package me.heymrau.seniorparkour.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemBuilder(Material material) {
        this(material, 1);
    }

    private ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }

    private ItemBuilder(Material material, int amount, int damage) {
        this(new ItemStack(material, amount, (byte) damage));
    }

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder from(ConfigurationSection section) {
        ItemBuilder itemBuilder = new ItemBuilder(new ItemStack(Material.getMaterial(section.getString("type", "AIR")), section.getInt("amount", 1)))
                .setName(section.getString("name"))
                .setLore(section.getStringList("lore"));

        if (section.isSet("model")) itemBuilder.itemMeta.setCustomModelData(section.getInt("model"));
        if (section.getBoolean("glow", false)) itemBuilder.addGlow();
        if (section.isConfigurationSection("enchantments")) {
            section.getConfigurationSection("enchantments")
                    .getKeys(false)
                    .forEach(key -> itemBuilder.putEnchant(Enchantment.getByName(key), section.getInt("enchantments." + key)));
        }

        return itemBuilder;
    }

    public ItemBuilder replaceValues(Map<String, String> replacements) {
        for (Map.Entry<String, String> replacementEntry : replacements.entrySet()) {
            String key = '%' + replacementEntry.getKey() + '%';
            String value = replacementEntry.getValue();
            if (getName().contains(key)) setName(getName().replace(key, value));
        }
        replaceLore(replacements);

        return this;
    }

    public void replaceLore(Map<String, String> replacementEntry) {
        setLore(ReplacementUtil.replaceValues(getLoreList(), replacementEntry));
    }

    @NotNull
    private String colorize(String textToTranslate) {
        return ChatColor.translateAlternateColorCodes('&', textToTranslate);
    }

    @NotNull
    public String getName() {
        return itemMeta.getDisplayName();
    }

    public ItemBuilder setName(String name) {
        if (name != null) itemMeta.setDisplayName(colorize(name));
        return this;
    }

    public ItemBuilder clearName() {
        itemMeta.setDisplayName(null);
        return this;
    }

    public boolean hasName() {
        return itemMeta.hasDisplayName();
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public ItemBuilder setAmount(int amount) {
        Validate.isTrue(amount >= 0, "The value must be greater than zero: ", amount);

        itemStack.setAmount(amount);
        return this;
    }

    @NotNull
    public List<String> getLoreList() {
        return itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
    }

    public ItemBuilder setLore(List<String> loreList) {
        if (loreList == null) return this;
        itemMeta.setLore(loreList.stream().map(this::colorize).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder addLore(String lore) {
        if (lore == null) return this;
        List<String> loreList = (hasLore() ? getLoreList() : new ArrayList<>());
        loreList.add(lore);
        setLore(loreList);
        return this;
    }

    public ItemBuilder removeLore(String lore, boolean translateChatColor) {
        if (lore == null || !hasLore()) return this;
        List<String> loreList = getLoreList();
        loreList.remove(translateChatColor ? colorize(lore) : lore);
        setLore(loreList);
        return this;
    }

    public ItemBuilder clearLore() {
        itemMeta.setLore(new ArrayList<>());
        return this;
    }

    public boolean containsLore(String lore, boolean translateChatColor) {
        if (lore == null || !hasLore()) return false;
        return getLoreList().contains(translateChatColor ? colorize(lore) : lore);
    }

    public boolean hasLore() {
        return itemMeta.hasLore();
    }

    @NotNull
    public Map<Enchantment, Integer> getEnchants() {
        return itemMeta.getEnchants();
    }

    public ItemBuilder setEnchants(Map<Enchantment, Integer> enchants, boolean clear) {
        if (enchants == null) return this;

        if (clear) clearEnchants().setEnchants(enchants, false);
        else enchants.forEach(this::putEnchant);

        return this;
    }

    public ItemBuilder clearEnchants() {
        if (!hasEnchants()) return this;
        List<Enchantment> enchantments = new ArrayList<>(getEnchants().keySet());
        enchantments.forEach(this::removeEnchant);
        return this;
    }

    public ItemBuilder putEnchant(Enchantment enchantment, int level) {
        if (enchantment == null) return this;
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        if (enchantment == null) return this;
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    public int getEnchantLevel(Enchantment enchantment) {
        return itemMeta.getEnchantLevel(enchantment);
    }

    public boolean hasEnchant(Enchantment enchantment) {
        if (!hasEnchants()) return false;
        return itemMeta.hasEnchant(enchantment);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasEnchants() {
        return itemMeta.hasEnchants();
    }

    public ItemBuilder setHead(UUID uuid) {
        if (!(itemMeta instanceof SkullMeta skullMeta)) return this;

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        return this;
    }

    @NotNull
    public Collection<ItemFlag> getItemFlags() {
        return itemMeta.getItemFlags();
    }

    public boolean hasItemFlags() {
        return getItemFlags().size() > 0;
    }

    public ItemBuilder setItemFlags(Collection<ItemFlag> itemFlags, boolean clear) {
        if (itemFlags == null) return this;
        if (clear && hasItemFlags()) clearItemFlags();
        itemFlags.forEach(this::addItemFlag);
        return this;
    }

    public ItemBuilder clearItemFlags() {
        final Collection<ItemFlag> itemFlags = getItemFlags();
        if (!hasItemFlags()) return this;
        itemFlags.forEach(itemMeta::removeItemFlags);
        return this;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        if (itemFlag == null) return false;
        return itemMeta.hasItemFlag(itemFlag);
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        if (itemFlag == null) return this;
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        if (!hasItemFlag(itemFlag)) return this;
        itemMeta.removeItemFlags(itemFlag);
        return this;
    }

    public ItemBuilder addGlow() {
        if (!hasEnchants()) putEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1);
        addItemFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}