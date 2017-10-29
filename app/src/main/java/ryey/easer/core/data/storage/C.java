package ryey.easer.core.data.storage;

public class C extends ryey.easer.commons.C {
    public static final String TYPE = "type";
    public static final String EVENT = "event";
    public static final String ACTIVE = "active";
    public static final String TRIG = "trigger";
    public static final String AFTER = "after";

    public class TriggerType {
        public static final String T_RAW = "raw_event";
        public static final String T_PRE = "pre_defined";
    }
}
