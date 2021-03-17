package Method.Client;

import Method.Client.clickgui.ClickGui;
import Method.Client.managers.FileManager;
import Method.Client.managers.SettingsManager;
import Method.Client.module.ModuleManager;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.command.CommandManager;
import Method.Client.utils.Creativetabs.*;
import Method.Client.utils.CustomEventsHandler;
import Method.Client.utils.EventsHandler;
import Method.Client.utils.Factory.MethodConfig;
import Method.Client.utils.Screens.Custom.Esp.EspGui;
import Method.Client.utils.Screens.Custom.Packet.AntiPacketGui;
import Method.Client.utils.Screens.Custom.Search.SearchGui;
import Method.Client.utils.Screens.Custom.Xray.XrayGui;
import Method.Client.utils.Screens.NewScreen;
import Method.Client.utils.font.CFont;
import Method.Client.utils.proxy.ClientProxy;
import Method.Client.utils.proxy.IProxy;
import Method.Client.utils.system.Nan0EventRegister;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = Main.MODID,
        name = Main.NAME,
        version = Main.VERSION,
        acceptableRemoteVersions = "*",
        guiFactory = "Method.Client.utils.Factory.MethodGuiFactory"
)

public class Main {
    @SidedProxy(clientSide = "Method.Client.utils.proxy.ClientProxy",
            serverSide = "Method.Client.utils.proxy.ServerProxy")
    public static IProxy proxy;


    public static final String MODID = "method";
    public static final String NAME = "MethodCode";
    public static final String MODCREDITS = "MethodCode";
    public static final String VERSION = "0.1.0";
    public static final String MCVERSION = "1.12.2";
    public static final String MODURL = "http://Github.com/";
    public static final String MODLOGO = "assets/method/method.png";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.Instance
    public static Main instance;
    public static SettingsManager setmgr;

    public static EventsHandler Evtmgr;
    public static CustomEventsHandler Cevtmgr;

    public static OnscreenGUI OnscreenGUI;
    public static AntiPacketGui AntiPacketgui;
    public static XrayGui Xray;
    public static SearchGui Search;
    public static EspGui GuiEsp;
    public static ClickGui ClickGui;
    public static MethodConfig config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Initializing " + NAME + " Version " + VERSION);
        event.getModMetadata().autogenerated = false; // stops it from complaining about missing mcmod.info
        event.getModMetadata().name = NAME;
        event.getModMetadata().authorList.clear();
        event.getModMetadata().credits = MODCREDITS;
        event.getModMetadata().url = MODURL;
        event.getModMetadata().logoFile = MODLOGO;

        new CreativeTabBlocks();
        new CreativeTabItems();
        new CreativeTabBreak();
        new CreativeTabArmor();
        new CreativeTabFun();

        config = new MethodConfig();
        config.preInit(event);

        Minecraft.getMinecraft().gameSettings.guiScale = 2;
        CFont.setupfont();


        MinecraftForge.EVENT_BUS.register(this);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        LOGGER.info("Setup " + NAME + " Version " + VERSION);

        proxy.init(event);

        // Initialization For Event handlers!
        Evtmgr = new EventsHandler();
        Cevtmgr = new CustomEventsHandler();

        Xray = new XrayGui();
        Search = new SearchGui();
        AntiPacketgui = new AntiPacketGui();
        GuiEsp = new EspGui();

        setmgr = new SettingsManager(); // SettingsManager BEFORE ModuleManager loaded modules

        new ModuleManager();

        new NewScreen();


        OnscreenGUI = new OnscreenGUI();
        ClickGui = new ClickGui();

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("Final " + NAME + " Version " + VERSION);
        new FileManager(); // File manager last so it can put things where they need to go!
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, ClickGui);
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, OnscreenGUI);
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Evtmgr);
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Cevtmgr);
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, AntiPacketgui);
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, GuiEsp);
        Nan0EventRegister.register(MinecraftForge.EVENT_BUS, Xray);

        if (CommandManager.getInstance() == null)
            CommandManager.getInstance();

        Display.setTitle("Method Client");

        ClientProxy.Gl = ModuleManager.getModuleByName("ArmorRender");
    }
}
