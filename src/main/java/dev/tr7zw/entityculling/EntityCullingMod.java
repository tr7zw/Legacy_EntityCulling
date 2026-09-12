package dev.tr7zw.entityculling;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.*;

import org.apache.logging.log4j.*;
import org.lwjgl.input.*;
//? if forge {

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

//? }

//? if >= 1.8.9 && forge {

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//? } else if forge {
/*import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.common.gameevent.*;
*///? }
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
//? if = 1.12.2 {
/*
import net.minecraft.util.text.*;
 
*///? }

//? if forge {

@Mod(
        modid = EntityCullingMod.MODID,
        name = EntityCullingMod.NAME,
        version = "1.6.2",
        //? if >= 1.8.9 {
        
        clientSideOnly = true,
        
        //? }
        acceptableRemoteVersions = "*"
)

//? }
public class EntityCullingMod
//? if ornithe {
    //implements net.fabricmc.api.ClientModInitializer
//? }
{
    public static final String MODID = "entityculling";
    public static final String NAME = "EntityCulling";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    //? if forge {
    
    @Mod.Instance(MODID)
     
    //? }
    public static EntityCullingMod instance;
    private Path configFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Config config;

    public OcclusionCullingInstance culling;
    public CullTask cullTask;
    private Thread cullThread;

    public static boolean enabled = true; // public static to make it faster for the jvm
    private final KeyBinding keybind = new KeyBinding("key.entityculling.toggle", Keyboard.KEY_NONE, "text.entityculling.title");
    //public boolean debugHitboxes = false;

    // Stats
    public int renderedBlockEntities = 0;
    public int skippedBlockEntities = 0;
    public int renderedEntities = 0;
    public int skippedEntities = 0;
    //public int tickedEntities = 0;
    //public int skippedEntityTicks = 0;

    public EntityCullingMod() {
        instance = this;
        //? if ornithe {
        /*configFile = new File("config", "entityculling.json").toPath();
        preInit();
        *///? }
    }

    public void preInit() {
        if (Files.exists(configFile)) {
            try {
                config = gson.fromJson(Files.newBufferedReader(configFile), Config.class);
            } catch (IOException e) {
                LOGGER.error("Error while loading config! Creating a new one!", e);
            }
        }
        if (config == null) {
            config = new Config();
            writeConfig();
        } else {
            if (ConfigUpgrader.upgradeConfig(config)) {
                writeConfig(); // Config got modified
            }
        }
    }

    public void init() {
        culling = new OcclusionCullingInstance(config.tracingDistance, new Provider());
        cullTask = new CullTask(culling, config.blockEntityWhitelist);
        cullThread = new Thread(cullTask, "CullThread");
        cullThread.setUncaughtExceptionHandler((thread, ex) -> {
            LOGGER.error("The CullingThread has crashed! Please report the following stacktrace!", ex);
        });
        cullThread.start();

    }

    public void writeConfig() {
        try {
            Files.write(configFile, gson.toJson(config).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void addOverlayInfo(List<String> left) {
        Minecraft mc = Minecraft.getMinecraft();
        //? if >= 1.8.9 {
        
        if (!mc.gameSettings.showDebugInfo || mc.gameSettings.reducedDebugInfo || MinecraftUtil.getPlayer().hasReducedDebug()) {
            return;
        }
         
        //? } else {
        /*if (!mc.gameSettings.showDebugInfo) {
            return;
        }
        *///? }

        //? if = 1.12.2 {
        /*
        left.add("[Culling] Last pass: " + cullTask.lastTime + "ms");
        left.add("[Culling] Rendered Block Entities: " + renderedBlockEntities + " Skipped: " + skippedBlockEntities);
        left.add("[Culling] Rendered Entities: " + renderedEntities + " Skipped: " + skippedEntities);
        *///? } else {

        left.add("[Culling] Last pass: " + cullTask.lastTime + "ms");
        left.add("[Culling] Rendered Block Entities: " + renderedBlockEntities + " Skipped: " + skippedBlockEntities);
        left.add("[Culling] Rendered Entities: " + renderedEntities + " Skipped: " + skippedEntities);

        //? }

        renderedBlockEntities = 0;
        skippedBlockEntities = 0;
        renderedEntities = 0;
        skippedEntities = 0;
    }

    public void keyBindPressed() {
        if (keybind.isPressed()) {
            enabled = !enabled;
            //? if = 1.12.2 {
            /*if (enabled) {
                if (Minecraft.getMinecraft().ingameGUI != null) {
                    Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString("§aCulling on"));
                }
            } else {
                if (Minecraft.getMinecraft().ingameGUI != null) {
                    Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString("§cCulling off"));
                }
            }
            *///? } else {

            EntityPlayerSP player = MinecraftUtil.getPlayer();
            if (enabled) {
                if (player != null) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Culling on"));
                }
            } else {
                if (player != null) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Culling off"));
                }
            }

            //? }
        }
    }

    // Modloader hooks

    //? if ornithe {
/*
    @Override
    public void onInitializeClient() {
        init();
        // TODO: Register keybinds
        net.ornithemc.osl.lifecycle.api.client.ClientWorldEvents.TICK_END.register((world) -> {
            cullTask.requestCull = true;
        });
        net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents.TICK_END.register((client) -> {
            cullTask.requestCull = true;
        });
    }

    *///? }

    //? if forge {
    
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        //? if = 1.12.2 {
        //addOverlayInfo(event.getLeft());
        //? } else {
        addOverlayInfo(event.left);
        //? }
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        init();
        ClientRegistry.registerKeyBinding(keybind);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        configFile = event.getModConfigurationDirectory().toPath().resolve(MODID + ".json");
        preInit();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        keyBindPressed();
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        keyBindPressed();
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        cullTask.requestCull = true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        cullTask.requestCull = true;
    }
     
    //? }

}
