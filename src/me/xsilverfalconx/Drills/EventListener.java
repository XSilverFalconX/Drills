package me.xsilverfalconx.Drills;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class EventListener
        implements Listener {

    EventListener(Drills thisPlugin) {
        this.thisPlugin = thisPlugin;
    }

    public Permission Drill = new Permission("Drills.use");
    private Drills thisPlugin;
    public HashMap<String, Long> timedRun = new HashMap();
    Set<String> playersenabled = new HashSet();
    public HashMap<String, Location> ocation = new HashMap();
    Multimap<String, Location> brokenBlocks = ArrayListMultimap.create();
    long timeStarted;
    ChatColor gold = ChatColor.GOLD;
    ChatColor aqua = ChatColor.AQUA;
    String logo = aqua + "[" + gold + "Drills" + aqua + "]" + gold + ":";

    private class BlockReplaceRunnable
            implements Runnable {

        private Block block;

        private Player player;
        Block firstblock;

        BlockReplaceRunnable(Block start, Player p) {
            this.block = start;
            this.firstblock = start;
            this.player = p;
        }

        int pblocks = 0;

        public void replaceBlocks(Location l, World w) {
            double i = l.getY();
            while (i >= 1) {
                i--;
                Location d = new Location(w, l.getX(), i, l.getZ());
                if (d.getBlock().getType() == Material.COBBLE_WALL) {
                    if (thisPlugin.getConfig().contains("ReplacementMat")) {
                        Material m = Material.matchMaterial((thisPlugin.getConfig().getString("ReplacementMat")));
                        d.getBlock().setType(m);
                    }
                }
            }
        }

        public void checkRemove(Player p) {
            replaceBlocks(ocation.get(p.getName()), p.getWorld());
            if (playersenabled.contains(p.getName())) {
                playersenabled.remove(p.getName());
            }
            if (timedRun.containsKey(p.getName())) {
                timedRun.remove(p.getName());
            }
            if (ocation.containsKey(p.getName())) {
                ocation.remove(p.getName());
            }
            if (brokenBlocks.containsKey(p.getName())) {
                brokenBlocks.removeAll(p.getName());
            }
            if (pblocks > 0) {
                p.sendMessage(logo + " You mined a total of [" + aqua + pblocks + gold + "] blocks" + aqua + ".");
            }
        }

        public Boolean onList(String s) {
            if (playersenabled.contains(s)) {
                return true;
            }
            return false;
        }

        public Boolean isBuilt(Location l) {
            if (l.getBlock().getType() == Material.FENCE) {
                if (l.getBlock().getRelative(BlockFace.UP, 2).getType().equals(Material.REDSTONE_TORCH_ON)) {
                    if (l.getBlock().getRelative(BlockFace.UP, 1).getType().equals(Material.COBBLE_WALL)) {
                        if ((l.getBlock().getRelative(BlockFace.WEST, 1).getType().equals(Material.IRON_BLOCK)) || (firstblock.getRelative(BlockFace.NORTH, 1).getType().equals(Material.IRON_BLOCK))) {
                            if ((l.getBlock().getRelative(BlockFace.EAST, 1).getType().equals(Material.IRON_BLOCK)) || (firstblock.getRelative(BlockFace.SOUTH, 1).getType().equals(Material.IRON_BLOCK))) {
                                return true;
                            }
                            player.sendMessage(logo + " broken parts, needs repairs");
                        } else {
                            player.sendMessage(logo + " broken parts, needs repairs");
                        }
                    } else {
                        player.sendMessage(logo + " broken parts, needs repairs");
                    }
                } else {
                    player.sendMessage(logo + " broken parts, needs repairs");
                }
            } else {
                player.sendMessage(logo + " broken parts, needs repairs");
            }
            return false;
        }

        public Boolean Minables(Material m) {
            if (thisPlugin.getConfig().getList("Minables") != null) {
                if (thisPlugin.getConfig().getList("Minables").size() > 0) {
                    int i = thisPlugin.getConfig().getList("Minables").size();
                    while (i >= 1) {
                        i--;
                        if (thisPlugin.getConfig().getList("Minables").get(i).equals(m.toString())) {
                            break;
                        }
                    }
                    if (thisPlugin.getConfig().getList("Minables").get(i).equals(m.toString())) {
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }

        public Boolean Unminables(Material m) {
            if (thisPlugin.getConfig().getList("UnMinables") != null) {
                if (thisPlugin.getConfig().getList("UnMinables").size() > 0) {
                    int i = thisPlugin.getConfig().getList("UnMinables").size();
                    while (i >= 1) {
                        i--;
                        if (thisPlugin.getConfig().getList("UnMinables").get(i).equals(m.toString())) {
                            break;
                        }
                    }
                    if (thisPlugin.getConfig().getList("UnMinables").get(i).equals(m.toString())) {
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }

        public Location checkFurnace() {
            BlockState NEstate = firstblock.getRelative(BlockFace.EAST, 1).getRelative(BlockFace.UP, 1).getState();
            BlockState NWstate = firstblock.getRelative(BlockFace.WEST, 1).getRelative(BlockFace.UP, 1).getState();
            BlockState NNstate = firstblock.getRelative(BlockFace.NORTH, 1).getRelative(BlockFace.UP, 1).getState();
            BlockState NSstate = firstblock.getRelative(BlockFace.SOUTH, 1).getRelative(BlockFace.UP, 1).getState();
            if ((NEstate instanceof Furnace)) {
                Furnace Nfurnace = (Furnace) NEstate;
                return Nfurnace.getLocation();
            } else if ((NWstate instanceof Furnace)) {
                Furnace Sfurnace = (Furnace) NWstate;
                return Sfurnace.getLocation();
            } else if ((NNstate instanceof Furnace)) {
                Furnace Rfurnace = (Furnace) NNstate;
                return Rfurnace.getLocation();
            } else if ((NSstate instanceof Furnace)) {
                Furnace Lfurnace = (Furnace) NSstate;
                return Lfurnace.getLocation();
            } else {
                return null;
            }
        }

        public Boolean furnaceConf() {
            if (thisPlugin.getConfig().contains("Furnaces")) {
                if (thisPlugin.getConfig().getBoolean("Furnaces")) {
                    return true;
                }
            }
            return false;
        }

        public boolean quiet() {
            if (thisPlugin.getConfig().contains("Quiet-Mode")) {
                if (thisPlugin.getConfig().getBoolean("Quiet-Mode")) {
                    return true;
                }
                return false;
            }

            return false;
        }

        public void processItem(Player p, Material m, int i) {
            Inventory inv = p.getInventory();
            if (inv.firstEmpty() != -1) {
                ItemStack iss = new ItemStack(m, 1);
                inv.addItem(iss);
            } else {
                p.sendMessage(logo + " You dont have enough space for the drill's upkeep. Now destroying items.");
            }
        }

        public void checkBrokenBlocks() {
            if (onList(player.getName())) {
                if (isBuilt(firstblock.getLocation())) {
                    if (ocation.containsKey(player.getName())) {
                        if (brokenBlocks.containsKey(player.getName())) {
                            if (!brokenBlocks.get(player.getName()).isEmpty()) {
                                for (Location l : brokenBlocks.get(player.getName())) {
                                    Location l2 = new Location(player.getWorld(), l.getX(), l.getY() + 1, l.getZ());
                                    if (l2.getBlock().getType() == Material.AIR) {
                                        checkRemove(player);
                                        player.sendMessage(logo + " A drill bit was broken. Stopping...");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        public void run() {
            checkBrokenBlocks();
            if (onList(player.getName())) {
                if (block.getLocation().equals(ocation.get(player.getName()))) {
                    player.sendMessage(logo + " " + gold + "[" + ChatColor.GREEN + "On" + ChatColor.GOLD + "] Starting" + aqua + "...");
                    if (thisPlugin.getConfig().getInt("WarmUpTime") > 0) {
                        player.sendMessage(logo + " This should only take " + "[" + aqua + thisPlugin.getConfig().getInt("WarmUpTime") + ChatColor.GOLD + "] seconds" + aqua + ".");
                        thisPlugin.getServer().getScheduler().runTaskLater(thisPlugin, new Runnable() {
                            @Override
                            public void run() {
                                checkFuel();
                            }
                        }, (20 * thisPlugin.getConfig().getInt("WarmUpTime") * 1L));
                    } else {
                        checkFuel();
                    }
                } else if (Minables(block.getType())) {
                    if (block.getType() == Material.REDSTONE_ORE) {
                        processItem(player, Material.REDSTONE, block.getDrops().size());
                        if (!quiet()) {
                            player.sendMessage(logo + " Mined " + aqua + block.getType().toString().toLowerCase() + gold + ".");
                        }
                        block.setType(Material.COBBLE_WALL);
                        pblocks += 1;
                        checkFuel();
                        brokenBlocks.put(player.getName(), block.getLocation());
                    } else if (block.getType() == Material.LAPIS_ORE) {
                        Inventory inv = player.getInventory();
                        if (inv.firstEmpty() != -1) {
                            ItemStack iss = new ItemStack(Material.INK_SACK, block.getDrops().size());
                            Short s = 4;
                            iss.setDurability(s);
                            inv.addItem(iss);
                        } else {
                            player.sendMessage(logo + " Not enough space. Now destroying items.");
                        }
                        if (!quiet()) {
                            player.sendMessage(logo + " Mined " + aqua + block.getType().toString().toLowerCase() + gold + ".");
                        }
                        block.setType(Material.COBBLE_WALL);
                        // ADD ITEM**************
                        pblocks += 1;
                        checkFuel();
                        brokenBlocks.put(player.getName(), block.getLocation());
                    } else if (block.getType() == Material.DIAMOND_ORE) {
                        processItem(player, Material.DIAMOND, block.getDrops().size());
                        if (!quiet()) {
                            player.sendMessage(logo + " Mined " + aqua + block.getType().toString().toLowerCase() + gold + ".");
                        }
                        block.setType(Material.COBBLE_WALL);
                        // ADD ITEM**************
                        pblocks += 1;
                        checkFuel();
                        brokenBlocks.put(player.getName(), block.getLocation());
                    } else if (block.getType() == Material.COAL_ORE) {
                        processItem(player, Material.COAL, block.getDrops().size());
                        if (!quiet()) {
                            player.sendMessage(logo + " Mined " + aqua + block.getType().toString().toLowerCase() + gold + ".");
                        }
                        block.setType(Material.COBBLE_WALL);
                        pblocks += 1;
                        checkFuel();
                        brokenBlocks.put(player.getName(), block.getLocation());
                    } else if (block.getType() == Material.EMERALD_ORE) {
                        processItem(player, Material.EMERALD, block.getDrops().size());
                        if (!quiet()) {
                            player.sendMessage(logo + " Mined " + aqua + block.getType().toString().toLowerCase() + gold + ".");
                        }
                        block.setType(Material.COBBLE_WALL);
                        pblocks += 1;
                        checkFuel();
                        brokenBlocks.put(player.getName(), block.getLocation());
                    } else {
                        processItem(player, block.getType(), block.getDrops().size());
                        if (!quiet()) {
                            player.sendMessage(logo + " Mined " + aqua + block.getType().toString().toLowerCase() + gold + ".");
                        }
                        block.setType(Material.COBBLE_WALL);
                        pblocks += 1;
                        checkFuel();
                        brokenBlocks.put(player.getName(), block.getLocation());
                    }
                } else if (Unminables(block.getType())) {
                    block.setType(Material.COBBLE_WALL);
                    if (!quiet()) {
                        player.sendMessage(logo + " Interacted with a unminable, continuing");
                    }
                    checkFuel();
                    brokenBlocks.put(player.getName(), block.getLocation());
                } else if (block.getType().equals(Material.BEDROCK)) {
                    if (!quiet()) {
                        player.sendMessage(logo + " Interacted with bedrock, continuing");
                    }
                    advance();
                    checkFuel();
                    brokenBlocks.put(player.getName(), block.getLocation());
                } else {
                    player.sendMessage(logo + " Failed to break " + aqua + block.getType().toString().toLowerCase());
                    checkRemove(player);
                }

            }
        }

        public void checkFuel() {
            //Removal Code for hashmap
            if (furnaceConf()) {
                if (thisPlugin.getConfig().contains("Fuel-Checker")) {
                    int cooldownTime = (int) thisPlugin.getConfig().getDouble("Fuel-Checker") * 60;
                    if (checkFurnace() != null) {
                        if (timedRun.containsKey(player.getName())) {
                            long cooldownRemaining = (timedRun.get(player.getName())) / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                            if (cooldownRemaining <= 0) {
                                timedRun.remove(player.getName());
                                advance();
                            } else {
                                advance();
                            }
                            //end removal code
                        } else {
                            if ((checkFurnace().getBlock().getState() instanceof Furnace)) {
                                Furnace fur = (Furnace) checkFurnace().getBlock().getState();
                                if (fur.getInventory().getFuel() != null) {
                                    if (fur.getInventory().getFuel().getAmount() >= 1) {
                                        if (thisPlugin.getConfig().getList("Furnace-fuel-types") != null) {
                                            if (thisPlugin.getConfig().getList("Furnace-fuel-types").contains(fur.getInventory().getFuel().getType().toString())) {
                                                ItemStack ll = new ItemStack(fur.getInventory().getFuel().getType(), fur.getInventory().getFuel().getAmount() - 1);
                                                fur.getInventory().setFuel(ll);
                                                player.sendMessage(ChatColor.YELLOW + logo + " Your Drill burns a piece of fuel");
                                                timeStarted = System.currentTimeMillis();
                                                timedRun.put(player.getName(), System.currentTimeMillis());
                                                advance();
                                            } else {
                                                player.sendMessage(logo + " Error: invalid fuel type!");
                                                checkRemove(player);
                                            }
                                        } else {
                                            System.out.println(logo + " Error Fuel types list is empty! Furnaces are now functioning as if they dont exist!");
                                            advance();
                                        }
                                    } else {
                                        player.sendMessage(logo + " Your drill has run out of fuel.");
                                        checkRemove(player);
                                    }
                                } else {
                                    player.sendMessage(logo + " There is no fuel.");
                                    checkRemove(player);
                                }
                            } else {
                                player.sendMessage(logo + " There is no furnace.");
                                checkRemove(player);
                            }
                        }
                    } else {
                        player.sendMessage(logo + " There is an invalid furnace.");
                        checkRemove(player);
                    }

                }
            } else {
                advance();
            }
        }

        private void advance() {
            if (block.getY() != 0) {
                if (player.isOnline()) {
                    if (isBuilt(firstblock.getLocation())) {
                        Location blo = block.getLocation();
                        player.getWorld().playSound(blo, Sound.DIG_STONE, 5, 5);
                        thisPlugin.getServer().getScheduler().scheduleSyncDelayedTask(thisPlugin, this, thisPlugin.getConfig().getInt("Speed") * 20L);
                        block = block.getRelative(BlockFace.DOWN);
                    } else {
                        checkRemove(player);
                    }
                    if (thisPlugin.getConfig().getBoolean("Logger")) {
                        thisPlugin.getLogger().log(Level.INFO, player.getName() + " Mined " + block.getType().toString().toLowerCase());
                    }
                    if ((thisPlugin.getConfig().getBoolean("Exp"))
                            && (!thisPlugin.getConfig().getList("Exp-On-Blocks").isEmpty())
                            && (thisPlugin.getConfig().getList("Exp-On-Blocks").contains(block.getType().toString()))
                            && (thisPlugin.getConfig().getInt("Exp-Amount") > 0)) {
                        player.giveExp(thisPlugin.getConfig().getInt("Exp-Amount"));
                        player.sendMessage(ChatColor.GREEN + logo + " " + thisPlugin.getConfig().getInt("Exp-Amount") + " EXP added for drilling " + block.getType());
                    }
                }
            } else if (block.getY() == 0) {
                player.sendMessage(logo + " Run complete!");
                checkRemove(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public final void handle(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            Block b = event.getClickedBlock();
            if ((p.hasPermission(Drill))
                    && (b.getType() == Material.FENCE)
                    && (b.getRelative(BlockFace.UP, 2).getType().equals(Material.REDSTONE_TORCH_ON))
                    && (b.getRelative(BlockFace.UP, 1).getType().equals(Material.COBBLE_WALL))
                    && ((b.getRelative(BlockFace.WEST, 1).getType().equals(Material.IRON_BLOCK)) || (b.getRelative(BlockFace.NORTH, 1).getType().equals(Material.IRON_BLOCK)))
                    && ((b.getRelative(BlockFace.EAST, 1).getType().equals(Material.IRON_BLOCK)) || (b.getRelative(BlockFace.SOUTH, 1).getType().equals(Material.IRON_BLOCK)))) {
                if (!playersenabled.contains(p.getName())) {
                    if (!ocation.containsValue(event.getClickedBlock().getLocation())) {
                        timeStarted = System.currentTimeMillis();
                        playersenabled.add(p.getName());
                        ocation.put(p.getName(), event.getClickedBlock().getLocation());
                        new BlockReplaceRunnable(event.getClickedBlock(), p).run();

                    } else {
                        p.sendMessage(logo + " Error, someone is already running that drill!");
                    }
                } else {
                    p.sendMessage(logo + " Error, you already have a drill running!");
                }

            }
        } else {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Player player = event.getPlayer();
                Block b = event.getClickedBlock();
                if ((player.hasPermission(Drill))
                        && (b.getType() == Material.FENCE)
                        && (b.getRelative(BlockFace.UP, 2).getType().equals(Material.REDSTONE_TORCH_ON))
                        && (b.getRelative(BlockFace.UP, 1).getType().equals(Material.COBBLE_WALL))
                        && ((b.getRelative(BlockFace.WEST, 1).getType().equals(Material.IRON_BLOCK)) || (b.getRelative(BlockFace.NORTH, 1).getType().equals(Material.IRON_BLOCK)))
                        && ((b.getRelative(BlockFace.EAST, 1).getType().equals(Material.IRON_BLOCK)) || (b.getRelative(BlockFace.SOUTH, 1).getType().equals(Material.IRON_BLOCK)))) {
                    if (playersenabled.contains(player.getName())) {
                        if (ocation.containsKey(player.getName())) {
                            if (ocation.get(player.getName()).equals(event.getClickedBlock().getLocation())) {
                                player.sendMessage(logo + " " + gold + "[" + ChatColor.RED + "Off" + ChatColor.GOLD + "] Stopping" + aqua + "...");
                                if (playersenabled.contains(player.getName())) {
                                    playersenabled.remove(player.getName());
                                }
                                if (timedRun.containsKey(player.getName())) {
                                    timedRun.remove(player.getName());
                                }
                                if (brokenBlocks.containsKey(player.getName())) {
                                    brokenBlocks.removeAll(player.getName());
                                }
                                if (ocation.containsKey(player.getName())) {
                                    double i = ocation.get(player.getName()).getY();
                                    while (i >= 1) {
                                        i--;
                                        Location d = new Location(player.getWorld(), ocation.get(player.getName()).getX(), i, ocation.get(player.getName()).getZ());
                                        if (d.getBlock().getType() == Material.COBBLE_WALL) {
                                            if (thisPlugin.getConfig().contains("ReplacementMat")) {
                                                Material m = Material.matchMaterial((thisPlugin.getConfig().getString("ReplacementMat")));
                                                d.getBlock().setType(m);
                                            } else {
                                                d.getBlock().setType(Material.STONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(logo + " You dont have any drill running to stop.");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public final void handle(PlayerQuitEvent event
    ) {
        if ((event.getPlayer() instanceof Player)) {
            Player player = event.getPlayer();
            if (ocation.containsKey(player.getName())) {
                System.out.println("[Drills]: Player " + player.getName() + " left while drill was running.");
                System.out.println("[Drills]: Cleaning...");
                double i = ocation.get(player.getName()).getY();
                while (i >= 1) {
                    i--;
                    Location d = new Location(player.getWorld(), ocation.get(player.getName()).getX(), i, ocation.get(player.getName()).getZ());
                    if (d.getBlock().getType() == Material.COBBLE_WALL) {
                        if (thisPlugin.getConfig().contains("ReplacementMat")) {
                            Material m = Material.matchMaterial((thisPlugin.getConfig().getString("ReplacementMat")));
                            d.getBlock().setType(m);
                        } else {
                            d.getBlock().setType(Material.STONE);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public final void handle(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (playersenabled.contains(p.getName())) {
            playersenabled.remove(p.getName());
        }
        if (timedRun.containsKey(p.getName())) {
            timedRun.remove(p.getName());
        }
        if (ocation.containsKey(p.getName())) {
            ocation.remove(p.getName());
        }
        if (brokenBlocks.containsKey(p.getName())) {
            brokenBlocks.removeAll(p.getName());
        }
    }
}
