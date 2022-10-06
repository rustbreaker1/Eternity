package me.cedric.siegegame.territory;

import me.cedric.siegegame.SiegeGame;
import me.cedric.siegegame.player.GamePlayer;
import me.deltaorion.bukkit.item.EMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TerritoryBlockers implements Listener {

    public final SiegeGame plugin;
    private final Territory territory;

    private final static List<Material> interactProhibited = new ArrayList<>();

    public TerritoryBlockers(SiegeGame plugin, Territory territory) {
        this.plugin = plugin;
        this.territory = territory;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        evaluateCancel(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        evaluateCancel(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Location loc = event.getInventory().getLocation();

        if (loc == null)
            return;

        if (!interactProhibited.contains(loc.getBlock().getType()))
            return;

        evaluateCancel((Player) event.getPlayer(), loc, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketFill(PlayerBucketFillEvent event) {
        evaluateCancel(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        evaluateCancel(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();

        if (item != null && interactProhibited.contains(item.getType()))
            evaluateCancel(event.getPlayer(), event.getPlayer().getLocation(), event);

        if (block != null && interactProhibited.contains(block.getType()))
            evaluateCancel(event.getPlayer(), block.getLocation(), event);
    }

    private void evaluateCancel(Player player, Location location, Cancellable event) {
        GamePlayer gamePlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        evaluateCancel(gamePlayer, location, event);
    }

    private void evaluateCancel(GamePlayer gamePlayer, Location location, Cancellable event) {
        if (location == null || !territory.isInside(location.getWorld(), location.getBlockX(), location.getBlockZ()))
            return;

        if (gamePlayer.isDead() || !gamePlayer.hasTeam())
            event.setCancelled(true);

        if (gamePlayer.getTeam().equals(territory.getTeam()))
            return;

        event.setCancelled(true);
    }

    static {
        // Regular blocks
        interactProhibited.add(Material.HONEYCOMB);
        interactProhibited.add(Material.ARMOR_STAND);
        interactProhibited.add(Material.END_CRYSTAL);
        interactProhibited.add(Material.CAKE);
        interactProhibited.add(Material.CANDLE);
        interactProhibited.add(Material.CHEST);

        // Pressure plates
        interactProhibited.add(Material.ACACIA_PRESSURE_PLATE);
        interactProhibited.add(Material.MANGROVE_PRESSURE_PLATE);
        interactProhibited.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        interactProhibited.add(Material.DARK_OAK_PRESSURE_PLATE);
        interactProhibited.add(Material.BIRCH_PRESSURE_PLATE);
        interactProhibited.add(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        interactProhibited.add(Material.WARPED_PRESSURE_PLATE);
        interactProhibited.add(Material.OAK_PRESSURE_PLATE);
        interactProhibited.add(Material.SPRUCE_PRESSURE_PLATE);
        interactProhibited.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        interactProhibited.add(Material.STONE_PRESSURE_PLATE);
        interactProhibited.add(Material.JUNGLE_PRESSURE_PLATE);
        interactProhibited.add(Material.CRIMSON_PRESSURE_PLATE);

        // Buttons
        interactProhibited.add(Material.ACACIA_BUTTON);
        interactProhibited.add(Material.DARK_OAK_BUTTON);
        interactProhibited.add(Material.STONE_BUTTON);
        interactProhibited.add(Material.POLISHED_BLACKSTONE_BUTTON);
        interactProhibited.add(Material.JUNGLE_BUTTON);
        interactProhibited.add(Material.BIRCH_BUTTON);
        interactProhibited.add(Material.CRIMSON_BUTTON);
        interactProhibited.add(Material.MANGROVE_BUTTON);
        interactProhibited.add(Material.OAK_BUTTON);
        interactProhibited.add(Material.SPRUCE_BUTTON);
        interactProhibited.add(Material.WARPED_BUTTON);

        // Signs
        interactProhibited.add(EMaterial.SIGN_BLOCK.getBukkitMaterial());
        interactProhibited.add(EMaterial.JUNGLE_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.CRIMSON_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.DARK_OAK_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.WARPED_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.OAK_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.MANGROVE_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.SPRUCE_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.BIRCH_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.MANGROVE_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.ACACIA_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.SPRUCE_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.ACACIA_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.CRIMSON_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.WARPED_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.JUNGLE_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.DARK_OAK_WALL_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.OAK_SIGN.getBukkitMaterial());
        interactProhibited.add(EMaterial.BIRCH_WALL_SIGN.getBukkitMaterial());

        // Fence gates
        interactProhibited.add(EMaterial.OAK_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.SPRUCE_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.BIRCH_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.JUNGLE_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.ACACIA_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.DARK_OAK_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.CRIMSON_FENCE_GATE.getBukkitMaterial());
        interactProhibited.add(EMaterial.WARPED_FENCE_GATE.getBukkitMaterial());

        // Doors and trapdoors
        interactProhibited.add(EMaterial.IRON_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.OAK_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.SPRUCE_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.BIRCH_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.JUNGLE_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.ACACIA_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.DARK_OAK_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.CRIMSON_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.WARPED_DOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.IRON_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.OAK_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.SPRUCE_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.BIRCH_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.JUNGLE_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.ACACIA_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.DARK_OAK_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.CRIMSON_TRAPDOOR.getBukkitMaterial());
        interactProhibited.add(EMaterial.WARPED_TRAPDOOR.getBukkitMaterial());
    }

}
