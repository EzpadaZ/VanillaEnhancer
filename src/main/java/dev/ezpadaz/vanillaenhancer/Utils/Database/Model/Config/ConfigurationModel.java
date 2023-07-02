package dev.ezpadaz.vanillaenhancer.Utils.Database.Model.Config;

import dev.ezpadaz.vanillaenhancer.Utils.MessageHelper;
import org.bson.Document;

public class ConfigurationModel {
    private boolean debugMode = true;

    // dxp settings
    private int dxp_multiplier = 2;
    private int dxp_death_cooldown = 3;
    private boolean dxp_enabled = true;
    private boolean dxp_aurelium_enabled = true;


    // tp settings
    private boolean tp_enabled = true;
    private String tp_material = "gold_ingot";

    private String tp_material_name = "Lingotes de oro";
    private int tp_cost = 4;
    private int tp_threshold = 60;

    private int tp_delay = 1;

    // gameplay enhancements
    private boolean infinity = true;
    private boolean cb_fix = true;

    // overseer settings
    private boolean overseer_enabled = true;
    private double warning_tps = 18.0;
    private double min_tps = 17.5;
    private int overseer_interval = 30;


    public int getOverseer_interval() {
        return overseer_interval;
    }

    public boolean getDebugMode() {
        return debugMode;
    }

    public int getDxp_multiplier() {
        return dxp_multiplier;
    }
    public int getDxp_death_cooldown() {
        return dxp_death_cooldown;
    }

    private boolean getDxp_enabled() {
        return dxp_enabled;
    }

    public boolean getDxp_aurelium_enabled() {
        return dxp_aurelium_enabled;
    }

    public boolean getTp_enabled() {
        return tp_enabled;
    }

    public int getTp_threshold() {
        return tp_threshold;
    }

    public String getTp_material_name() { return tp_material_name; }

    public int getTp_delay() {
        return tp_delay;
    }

    public String getTp_material() {
        return tp_material;
    }

    public int getTp_cost() {
        return tp_cost;
    }

    public boolean getInfinity() {
        return infinity;
    }

    public boolean getCb_fix() {
        return cb_fix;
    }

    public boolean getOverseer_enabled() {
        return overseer_enabled;
    }

    public double getWarning_tps() {
        return warning_tps;
    }

    public double getMin_tps() {
        return min_tps;
    }


    public ConfigurationModel() {
        // this loads defaults.
    }

    public ConfigurationModel(
            boolean debugMode,
            int dxp_multiplier,
            int dxp_death_cooldown,
            boolean dxp_enabled,
            boolean dxp_aurelium_enabled,
            boolean tp_enabled,
            int tp_delay,
            int tp_threshold,
            String tp_material,
            String tp_material_name,
            int tp_cost,
            boolean infinity,
            boolean cb_fix,
            boolean overseer_enabled,
            int overseer_interval,
            double warning_tps,
            double min_tps
    ) {
        // this modifies values.
        this.debugMode = debugMode;
        this.dxp_multiplier = dxp_multiplier;
        this.dxp_death_cooldown = dxp_death_cooldown;
        this.dxp_enabled = dxp_enabled;
        this.dxp_aurelium_enabled = dxp_aurelium_enabled;
        this.tp_enabled = tp_enabled;
        this.tp_material = tp_material;
        this.tp_material_name = tp_material_name;
        this.tp_delay = tp_delay;
        this.tp_threshold = tp_threshold;
        this.tp_cost = tp_cost;
        this.infinity = infinity;
        this.cb_fix = cb_fix;
        this.overseer_enabled = overseer_enabled;
        this.warning_tps = warning_tps;
        this.min_tps = min_tps;
    }

    // HOW TO ADD A NEW SETTING
    // ADD IT ABOVE AS A VALUE
    // ADD IT TO THE CONSTRUCTOR
    // ADD IT TO ITS RESPECTIVE getDefaultXXXXSetting() METHOD.
    // PARSE IT TO THE CONSTRUCTOR FROM THE  getFromDocument METHOD (APPLYING A DEFAULT VALUE IF IT NOT FOUND THE FIRST TIME.)

    public ConfigurationModel getFromDocument(Document document) {
        if (document == null) {
            MessageHelper.consoleDebug("CONFIG ERROR, RETURNED DEFAULT VALUES.");
            return new ConfigurationModel();
        }

        Document dxp = document.get("double-xp", Document.class) != null ? document.get("double-xp", Document.class) : getDefaultDXPSettings();
        Document tp = document.get("tp", Document.class) != null ? document.get("tp", Document.class) : getDefaultTPSettings();
        Document enhancements = document.get("enhancements", Document.class) != null ? document.get("enhancements", Document.class) : getDefaultEnhancementsSettings();
        Document overseer = document.get("overseer", Document.class) != null ? document.get("overseer", Document.class) : getDefaultOverseerSettings();

        // Yes, parameters even after they are ASSIGNED, need to be in order.
        ConfigurationModel temp = new ConfigurationModel(
                debugMode = document.getBoolean("debug", true),
                dxp_multiplier = dxp.getInteger("multiplier", 2),
                dxp_death_cooldown = dxp.getInteger("death_cooldown", 3),
                dxp_enabled = dxp.getBoolean("enabled", true),
                dxp_aurelium_enabled = dxp.getBoolean("aurelium_enabled", true),
                tp_enabled = tp.getBoolean("enabled", true),
                tp_delay = tp.getInteger("delay", 1),
                tp_threshold = tp.getInteger("threshold", 60),
                tp_material = getStringOrDefault(tp, "material", "gold_ingot"),
                tp_material_name = getStringOrDefault(tp, "material_name", "Lingotes de oro"),
                tp_cost = tp.getInteger("cost", 4),
                infinity = enhancements.getBoolean("infinity", true),
                cb_fix = enhancements.getBoolean("cb_fix", true),
                overseer_enabled = overseer.getBoolean("enabled", true),
                overseer_interval = overseer.getInteger("interval", 30),
                warning_tps = getDoubleOrDefault(overseer, "warning_tps", 18.0),
                min_tps = getDoubleOrDefault(overseer, "min_tps", 17.5)
        );
        return temp;
    }

    private String getStringOrDefault(Document document, String key, String defaultValue) {
        Object value = document.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return defaultValue;
    }

    private double getDoubleOrDefault(Document document, String key, double defaultValue) {
        Object value = document.get(key);
        if (value instanceof Double) {
            return (Double) value;
        }
        return defaultValue;
    }

    public Document getDocumentFormat() {
        Document settings = new Document("debug", getDebugMode());
        settings.append("double-xp", getDefaultDXPSettings());
        settings.append("tp", getDefaultTPSettings());
        settings.append("overseer", getDefaultOverseerSettings());
        settings.append("enhancements", getDefaultEnhancementsSettings());
        return settings;
    }

    Document getDefaultDXPSettings() {
        Document dxp = new Document("multiplier", getDxp_multiplier());
        dxp.append("death_cooldown", getDxp_death_cooldown());
        dxp.append("aurelium_enabled", getDxp_aurelium_enabled());
        dxp.append("enabled", getDxp_enabled());
        return dxp;
    }

    Document getDefaultTPSettings() {
        Document tpSettings = new Document("enabled", getTp_enabled());
        tpSettings.append("material", getTp_material());
        tpSettings.append("material_name", getTp_material_name());
        tpSettings.append("cost", getTp_cost());
        tpSettings.append("threshold", getTp_threshold());
        tpSettings.append("delay", getTp_delay());
        return tpSettings;
    }

    Document getDefaultEnhancementsSettings() {
        Document enhancements = new Document("infinity", getInfinity());
        enhancements.append("cb_fix", getCb_fix());
        return enhancements;
    }

    Document getDefaultOverseerSettings() {
        Document overseer = new Document("enabled", getOverseer_enabled());
        overseer.append("warning_tps", getWarning_tps());
        overseer.append("min_tps", getMin_tps());
        overseer.append("interval", getOverseer_interval());
        return overseer;
    }


}
