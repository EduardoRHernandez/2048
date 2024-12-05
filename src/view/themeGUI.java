package view;

import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

public class themeGUI {

  public static class Theme {

    private final String backgroundColor;
    private final String gridBackgroundColor;
    private final Map<Integer, String> tileColors;

    public Theme(
      String backgroundColor,
      String gridBackgroundColor,
      Map<Integer, String> tileColors
    ) {
      this.backgroundColor = backgroundColor;
      this.gridBackgroundColor = gridBackgroundColor;
      this.tileColors = tileColors;
    }

    public String getBackgroundColor() {
      return backgroundColor;
    }

    public String getGridBackgroundColor() {
      return gridBackgroundColor;
    }

    public String getTileColor(int value) {
      return tileColors.getOrDefault(value, "#cdc1b4");
    }
  }

  private final Map<String, Theme> themes;
  private Theme currentTheme;

  public themeGUI() {
    themes =
      Map.of(
        "Classic",
        new Theme(
          "#faf8ef",
          "#bbada0",
          Map.ofEntries(
            Map.entry(2, "#eee4da"),
            Map.entry(4, "#ede0c8"),
            Map.entry(8, "#f2b179"),
            Map.entry(16, "#f59563"),
            Map.entry(32, "#f67c5f"),
            Map.entry(64, "#f65e3b"),
            Map.entry(128, "#edcf72"),
            Map.entry(256, "#edcc61"),
            Map.entry(512, "#edc850"),
            Map.entry(1024, "#edc53f"),
            Map.entry(2048, "#edc22e")
          )
        ),
        "Dark",
        new Theme(
          "#222222",
          "#333333",
          Map.ofEntries(
            Map.entry(2, "#444444"),
            Map.entry(4, "#555555"),
            Map.entry(8, "#666666"),
            Map.entry(16, "#777777"),
            Map.entry(32, "#888888"),
            Map.entry(64, "#999999"),
            Map.entry(128, "#aaaaaa"),
            Map.entry(256, "#bbbbbb"),
            Map.entry(512, "#cccccc"),
            Map.entry(1024, "#dddddd"),
            Map.entry(2048, "#eeeeee")
          )
        ),
        "Christmas",
        new Theme(
          "#004d00", // Snowy white background
          "#d32f2f", // Red grid background
          Map.ofEntries(
            Map.entry(2, "#ffffff"), // Snow white tiles
            Map.entry(4, "#ffcccc"), // Light red tiles
            Map.entry(8, "#d32f2f"), // Bright red tiles
            Map.entry(16, "#388e3c"), // Green tiles
            Map.entry(32, "#66bb6a"), // Light green tiles
            Map.entry(64, "#8bc34a"), // Lime green tiles
            Map.entry(128, "#aed581"),
            Map.entry(256, "#ffab91"),
            Map.entry(512, "#ff7043"),
            Map.entry(1024, "#e64a19"),
            Map.entry(2048, "#bf360c")
          )
        ),
        "Halloween",
        new Theme(
          "#2b2b2b", // Dark gray background
          "#ff6f00", // Orange grid background
          Map.ofEntries(
            Map.entry(2, "#ffe0b2"), // Light orange tiles
            Map.entry(4, "#ffcc80"), // Brighter orange tiles
            Map.entry(8, "#ffab40"), // Deep orange tiles
            Map.entry(16, "#ff8a65"), // Light red-orange tiles
            Map.entry(32, "#d84315"), // Darker red-orange tiles
            Map.entry(64, "#ff5722"), // Fiery orange tiles
            Map.entry(128, "#e64a19"),
            Map.entry(256, "#bf360c"),
            Map.entry(512, "#a4510a"),
            Map.entry(1024, "#8a2b0e"),
            Map.entry(2048, "#571b0d")
          )
        )
      );

    currentTheme = themes.get("Classic"); // Default theme
  }

  public Theme getCurrentTheme() {
    return currentTheme;
  }

  public void setTheme(String themeName) {
    if (themes.containsKey(themeName)) {
      currentTheme = themes.get(themeName);
    }
  }

  public Map<String, Theme> getThemes() {
    return themes;
  }

  public void applyTheme(Parent root, Region grid) {
    if (root instanceof Region) {
      ((Region) root).setStyle(
          "-fx-background-color: " +
          getCurrentTheme().getBackgroundColor() +
          ";"
        );
    }
    grid.setStyle(
      "-fx-background-color: " +
      getCurrentTheme().getGridBackgroundColor() +
      ";"
    );
  }
}
