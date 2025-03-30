<p align="center">
  <image src="./media/logo.png" height="125" width="125"/>
  <h1 align="center">XPBoosters</h1>
</p>

XP Alchemy is a mod adding new player effects and 7 new potions: **XP Boost** and **XP Boost Potions**, **Experience Debt** and **Experience Debt Potions**.

**This mod is meant to be used with other mods like Hammer mods and Mending.**

![XPAlchemy](https://cdn.modrinth.com/data/zTbCfijz/images/ee1e1bb4676fd1b7ced15ac612f3b3a949ddd7c3.png)

### Features
- **XP Boost Potions**:
  - Tier 1: **5 minutes**, **x2 XP**.
  - Tier 2: **10 minutes**, **x2 XP**.
  - Tier 3: **15 minutes**, **x3 XP**.
  - Tier 4: **15 minutes**, **x4 XP**.
- Brewing recipes:
  - Tier 1: **Thick Potion** + **Glow Berries**
  - Tier 2: **Tier 1 Potion** + **Ghast Tears**
  - Tier 3: **Tier 2 Potion** + **Experience Bottles**
  - Tier 4: **Tier 3 Potion** + **Netherite Scrap**

- **Experience Debt Potions**: The health added by the potion is logarithmic. It is based on the amount of XP traded by the potion.
  - Tier 1: **Trades 20% of player's XP**
  - Tier 2: **Trades 40% of player's XP**
  - Tier 3: **Trades 60% of player's XP**
- Brewing recipes:
  - Tier 1: **Thick Potion** + **Nautilus Shell**
  - Tier 2: **Tier 1 Potion** + **Echo Shard**
  - Tier 3: **Tier 2 Potion** + **Heart of the Sea**


- XP Boost effect (`/effect add <player> xpalchemy:xp_boost_effect <duration> <amplifier>`)
  - `<amplifier>`: 0 = x2 XP, 1 = x3 XP, 2 = x4 XP, 3 = x5 XP, etc...
- Experience Debt Effect (`/effect add <player> xpalchemy:xp_health_debt_effect <duration> <amplifier>`)
  - This effect has no actual effect on duration, but it's a fancy way to see when you are using your Experience Debt.


- Translated in English (`en-us`) and French (`fr-fr`)

### Planned
- Allow the potion to spawn in dungeons
- Stuff Enchantments that multiplies XP obtained when wore (more likely armor enchantments, maybe items too?) **[Non-cumulative]**

For any suggestion, bug report, basically anything like that, please go to the [GitHub Issues page](https://github.com/VELD-Dev/XPAlchemy/issues).