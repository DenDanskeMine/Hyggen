package online.hyggen.hyggen_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Hyggen extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("The plugin is starting up.");

        // Register all of your listeners that exist in this class
        getServer().getPluginManager().registerEvents(this, this);
    }

    // Listener that is run when a PlayerJoinEvent is triggered.
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        // Get the joined player's name
        String playerName = event.getPlayer().getName();

        // Set a custom join message with the player's name
        event.setJoinMessage(playerName + " has joined the server.");

        System.out.println("Player " + playerName + " has joined the server.");
    }

    // Listener that is run when a PlayerInteractEvent is triggered.
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // Check if the player right-clicked a redstone block
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.REDSTONE_BLOCK) {
            // Open the GUI to the player
            openCustomGUI(event.getPlayer());
        }
    }

    // Listener that is run when a player interacts with the GUI
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is the custom GUI
        if (event.getView().getTitle().equals("Custom GUI")) {
            event.setCancelled(true); // Cancel the default action of the click event

            // Handle the specific item clicked within the GUI
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.DIAMOND) {
                // Generate a random item
                ItemStack randomItem = generateRandomItem();

                // Give the random item to the player
                Player player = (Player) event.getWhoClicked();
                player.getInventory().addItem(randomItem);

                // Close the GUI
                player.closeInventory();

                // Send a message to the player
                player.sendMessage("You received a random item!");
            }
        }
    }

    // Open a custom GUI to the player
    private void openCustomGUI(Player player) {
        // Create the inventory with a size of 9 slots (1 row)
        Inventory gui = Bukkit.createInventory(null, 9, "Custom GUI");

        // Add a diamond item to the GUI
        ItemStack diamondItem = new ItemStack(Material.DIAMOND);
        ItemMeta diamondMeta = diamondItem.getItemMeta();
        diamondMeta.setDisplayName("Diamond");
        diamondItem.setItemMeta(diamondMeta);
        gui.setItem(4, diamondItem);

        // Open the GUI to the player
        player.openInventory(gui);
    }

    // Generate a random item
    private ItemStack generateRandomItem() {
        // Get all available materials
        Material[] materials = Material.values();

        // Create a list to store the eligible materials
        List<Material> eligibleMaterials = new ArrayList<>();

        // Iterate through all materials and filter out unwanted ones
        for (Material material : materials) {
            if (material.isItem() && material != Material.AIR) {
                eligibleMaterials.add(material);
            }
        }

        // Get a random material from the eligible list
        Random random = new Random();
        Material randomMaterial = eligibleMaterials.get(random.nextInt(eligibleMaterials.size()));

        // Create the item stack with quantity 1
        ItemStack itemStack = new ItemStack(randomMaterial, 1);

        // Set the display name of the item
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(randomMaterial.toString());
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin stopped");
        // Plugin shutdown logic
    }
}
