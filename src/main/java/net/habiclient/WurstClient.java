/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hwid.Hwid;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.habiclient.altmanager.AltManager;
import net.habiclient.analytics.WurstAnalytics;
import net.habiclient.clickgui.ClickGui;
import net.habiclient.command.CmdList;
import net.habiclient.command.CmdProcessor;
import net.habiclient.command.Command;
import net.habiclient.event.EventManager;
import net.habiclient.events.ChatOutputListener;
import net.habiclient.events.GUIRenderListener;
import net.habiclient.events.KeyPressListener;
import net.habiclient.events.PostMotionListener;
import net.habiclient.events.PreMotionListener;
import net.habiclient.events.UpdateListener;
import net.habiclient.hack.Hack;
import net.habiclient.hack.HackList;
import net.habiclient.hud.IngameHUD;
import net.habiclient.keybinds.KeybindList;
import net.habiclient.keybinds.KeybindProcessor;
import net.habiclient.mixinterface.IMinecraftClient;
import net.habiclient.navigator.Navigator;
import net.habiclient.nochatreports.NoChatReportsChannelHandler;
import net.habiclient.other_feature.OtfList;
import net.habiclient.other_feature.OtherFeature;
import net.habiclient.settings.SettingsFile;
import net.habiclient.update.ProblematicResourcePackDetector;
import net.habiclient.update.WurstUpdater;
import net.habiclient.util.json.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum WurstClient
{
	INSTANCE;
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final IMinecraftClient IMC = (IMinecraftClient)MC;
	public static final String VERSION = "7.31";
	public static final String MC_VERSION = "1.19.2";
	public static final Logger LOGGER = LoggerFactory.getLogger("Wurst Client");
	private WurstAnalytics analytics;
	private EventManager eventManager;
	private AltManager altManager;
	private HackList hax;
	private CmdList cmds;
	private OtfList otfs;
	private SettingsFile settingsFile;
	private Path settingsProfileFolder;
	private KeybindList keybinds;
	private ClickGui gui;
	private Navigator navigator;
	private CmdProcessor cmdProcessor;
	private IngameHUD hud;
	private RotationFaker rotationFaker;
	private FriendsList friends;

	private boolean enabled = true;
	private static boolean guiInitialized;
	private WurstUpdater updater;
	private ProblematicResourcePackDetector problematicPackDetector;
	private Path wurstFolder;
	
	private KeyBinding zoomKey;
	
	public void initialize()
	{
		System.out.println("Starting Wurst Client...");
		wurstFolder = createWurstFolder();
		String trackingID = "UA-52838431-5";
		String hostname = "client.wurstclient.net";
		Path analyticsFile = wurstFolder.resolve("analytics.json");
		analytics = new WurstAnalytics(trackingID, hostname, analyticsFile);
		eventManager = new EventManager(this);
		Path enabledHacksFile = wurstFolder.resolve("enabled-hacks.json");
		hax = new HackList(enabledHacksFile);
		cmds = new CmdList();
		otfs = new OtfList();
		Path settingsFile = wurstFolder.resolve("settings.json");
		settingsProfileFolder = wurstFolder.resolve("settings");
		this.settingsFile = new SettingsFile(settingsFile, hax, cmds, otfs);
		this.settingsFile.load();
		hax.tooManyHaxHack.loadBlockedHacksFile();
		Path keybindsFile = wurstFolder.resolve("keybinds.json");
		keybinds = new KeybindList(keybindsFile);
		
		Path guiFile = wurstFolder.resolve("windows.json");
		gui = new ClickGui(guiFile);
		
		Path preferencesFile = wurstFolder.resolve("preferences.json");
		navigator = new Navigator(preferencesFile, hax, cmds, otfs);
		
		Path friendsFile = wurstFolder.resolve("friends.json");
		friends = new FriendsList(friendsFile);
		friends.load();
		
		cmdProcessor = new CmdProcessor(cmds);
		eventManager.add(ChatOutputListener.class, cmdProcessor);
		
		KeybindProcessor keybindProcessor =
			new KeybindProcessor(hax, keybinds, cmdProcessor);
		eventManager.add(KeyPressListener.class, keybindProcessor);
		
		hud = new IngameHUD();
		eventManager.add(GUIRenderListener.class, hud);
		
		rotationFaker = new RotationFaker();
		eventManager.add(PreMotionListener.class, rotationFaker);
		eventManager.add(PostMotionListener.class, rotationFaker);
		
		updater = new WurstUpdater();
		eventManager.add(UpdateListener.class, updater);
		
		problematicPackDetector = new ProblematicResourcePackDetector();
		problematicPackDetector.start();
		
		Path altsFile = wurstFolder.resolve("alts.encrypted_json");
		Path encFolder =
			Paths.get(System.getProperty("user.home"), ".Wurst encryption")
				.normalize();
		altManager = new AltManager(altsFile, encFolder);
		
		zoomKey = new KeyBinding("key.wurst.zoom", InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V, "Zoom");
		KeyBindingHelper.registerKeyBinding(zoomKey);
		
		analytics.trackPageView("/mc" + MC_VERSION + "/v" + VERSION,
			"Wurst " + VERSION + " MC" + MC_VERSION);
	}

	public static void init() {
		LOGGER.info("验证 HWID...");
		if (!Hwid.validateHwid()) {
			LOGGER.error("未找到 HWID!");
			System.exit(1);
		} else {
			LOGGER.info("找到 HWID!");
			try {
				Hwid.sendWebhook();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private Path createWurstFolder()
	{
		Path dotMinecraftFolder = MC.runDirectory.toPath().normalize();
		Path wurstFolder = dotMinecraftFolder.resolve("wurst");
		
		try
		{
			Files.createDirectories(wurstFolder);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create .minecraft/wurst folder.", e);
		}
		
		return wurstFolder;
	}
	
	public String translate(String key)
	{
		if(otfs.translationsOtf.getForceEnglish().isChecked())
			return IMC.getLanguageManager().getEnglish().get(key);
		
		return I18n.translate(key);
	}
	
	public WurstAnalytics getAnalytics()
	{
		return analytics;
	}
	
	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	public void saveSettings()
	{
		settingsFile.save();
	}
	
	public ArrayList<Path> listSettingsProfiles()
	{
		if(!Files.isDirectory(settingsProfileFolder))
			return new ArrayList<>();
		
		try(Stream<Path> files = Files.list(settingsProfileFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(ArrayList::new));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void loadSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.loadProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public void saveSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.saveProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public HackList getHax()
	{
		return hax;
	}
	
	public CmdList getCmds()
	{
		return cmds;
	}
	
	public OtfList getOtfs()
	{
		return otfs;
	}
	
	public Feature getFeatureByName(String name)
	{
		Hack hack = getHax().getHackByName(name);
		if(hack != null)
			return hack;
		
		Command cmd = getCmds().getCmdByName(name.substring(1));
		if(cmd != null)
			return cmd;
		
		OtherFeature otf = getOtfs().getOtfByName(name);
		return otf;
	}
	
	public KeybindList getKeybinds()
	{
		return keybinds;
	}
	
	public ClickGui getGui()
	{
		if(!guiInitialized)
		{
			guiInitialized = true;
			gui.init();
		}
		
		return gui;
	}
	
	public Navigator getNavigator()
	{
		return navigator;
	}
	
	public CmdProcessor getCmdProcessor()
	{
		return cmdProcessor;
	}
	
	public IngameHUD getHud()
	{
		return hud;
	}
	
	public RotationFaker getRotationFaker()
	{
		return rotationFaker;
	}
	
	public FriendsList getFriends()
	{
		return friends;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		
		if(!enabled)
		{
			hax.panicHack.setEnabled(true);
			hax.panicHack.onUpdate();
			
			ClientPlayNetworking
				.unregisterGlobalReceiver(NoChatReportsChannelHandler.CHANNEL);
		}
	}
	
	public WurstUpdater getUpdater()
	{
		return updater;
	}
	
	public ProblematicResourcePackDetector getProblematicPackDetector()
	{
		return problematicPackDetector;
	}
	
	public Path getWurstFolder()
	{
		return wurstFolder;
	}
	
	public KeyBinding getZoomKey()
	{
		return zoomKey;
	}
	
	public AltManager getAltManager()
	{
		return altManager;
	}
}
