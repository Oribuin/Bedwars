package in.oribu.bedwars.upgrade;

import in.oribu.bedwars.upgrade.impl.ForgeUpgrade;
import in.oribu.bedwars.upgrade.impl.HasteUpgrade;
import in.oribu.bedwars.upgrade.impl.HealUpgrade;
import in.oribu.bedwars.upgrade.impl.ProtectionUpgrade;
import in.oribu.bedwars.upgrade.impl.SharpnessUpgrade;

public enum UpgradeType {
    PROTECTION(new ProtectionUpgrade()), // Protection upgrade
    SHARPNESS(new SharpnessUpgrade()), // Sharpness upgrade
    HASTE(new HasteUpgrade()), // Haste upgrade
    FORGE(new ForgeUpgrade()), // Forge upgrade
    HEAL_STATION(new HealUpgrade()) // Heal station upgrade
    ;

    private final Upgrade upgrade;

    UpgradeType(Upgrade upgrade) {
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

}
