//package net.i_no_am.huds;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.font.TextRenderer;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.MathHelper;
//
//import java.util.Objects;
//
//public class HUDComponent {
//
//    private final MinecraftClient client;
//
//    public HUDComponent(MinecraftClient client) {
//        this.client = client;
//    }
//
//    public void render(MatrixStack matrices, int screenWidth, int screenHeight) {
//        TextRenderer textRenderer = client.textRenderer;
//        Text tpsText = Text.of("TPS: ").copy().append(getFormattedTPS());
//        Text pingText = Text.of("Ping: ").copy().append(getFormattedPing());
//        Text hourText = Text.of("Hour: ").copy().append(getFormattedHour());
//
//        int textHeight = textRenderer.fontHeight;
//
//        renderText(matrices, screenWidth - 5 - textRenderer.getWidth(tpsText), 5, tpsText, 0xFFFFFF);
//        renderText(matrices, screenWidth - 5 - textRenderer.getWidth(pingText), 5 + textHeight + 2, pingText, 0xFFFFFF);
//        renderText(matrices, screenWidth - 5 - textRenderer.getWidth(hourText), 5 + textHeight * 2 + 4, hourText, 0xFFFFFF);
//    }
//
//    private Text getFormattedTPS() {
//        double tps = MathHelper.clamp(client.getTickDelta(), 0.0, 20.0);
//        return Text.of(String.format("%.2f", tps));
//    }
//
//    private Text getFormattedPing() {
//        try {
//            return Text.of(String.valueOf(Objects.requireNonNull(client.getNetworkHandler()).getPlayerListEntry(Objects.requireNonNull(client.player).getGameProfile().getId()).getLatency()));
//        } catch (NullPointerException e) {
//            return Text.of("Ping: N/A");
//        }
//    }
//
//    private Text getFormattedHour() {
//        try {
//            long worldTime = Objects.requireNonNull(client.world).getTimeOfDay();
//            int hour = (int) ((worldTime / 1000L + 6L) % 24L);
//            return Text.of(String.valueOf(hour));
//        } catch (NullPointerException e) {
//            return Text.of("Hour: N/A");
//        }
//    }
//
//    private void renderText(MatrixStack matrices, int x, int y, Text text, int color) {
//        renderText(drawWithShadow(matrices, text, x, y, color);
//    }
//}