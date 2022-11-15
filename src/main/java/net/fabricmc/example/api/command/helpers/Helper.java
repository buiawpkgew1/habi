package net.fabricmc.example.api.utils;

import baritone.api.BaritoneAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Stream;

public interface Helper {
    /**
      * {@link Helper}的实例。用于静态上下文引用。
     */
    Helper HELPER = new Helper() {};

    /**
     * Instance of the game
     */
    Minecraft mc = Minecraft.getMinecraft();

    static ITextComponent Prefix() {
                // Inner text component
            final Calendar now = Calendar.getInstance();
                final boolean xd = now.get(Calendar.MONTH) == Calendar.APRIL && now.get(Calendar.DAY_OF_MONTH) <= 3;
                ITextComponent baritone = new TextComponentString(xd ? "Baritoe" : BaritoneAPI.getSettings().shortBaritonePrefix.value ? "B" : "Baritone");
                baritone.getStyle().setColor(TextFormatting.LIGHT_PURPLE);

        // Outer brackets
        ITextComponent prefix = new TextComponentString("");
                prefix.getStyle().setColor(TextFormatting.DARK_PURPLE);
                prefix.appendText("[");
                prefix.appendSibling(baritone);
                prefix.appendText("]");

                return prefix;
    }

    /**
067     * Send a message to display as a toast popup
068     *
069     * @param title   The title to display in the popup
070     * @param message The message to display in the popup
071     */

    default void logToast(ITextComponent title, ITextComponent message) {
                mc.addScheduledTask(() -> BaritoneAPI.getSettings().toaster.value.accept(title, message));
            }

    /**
077     * Send a message to display as a toast popup
078     *
079     * @param title   The title to display in the popup
080     * @param message The message to display in the popup
081     */
    default void logToast(String title, String message) {
                logToast(new TextComponentString(title), new TextComponentString(message));
    }

    /**
087     * Send a message to display as a toast popup
088     *
089     * @param message The message to display in the popup
090     */
    default void logToast(String message) {
                logToast(Helper.getPrefix(), new TextComponentString(message));
            }

    /**
096     * Send a message as a desktop notification
097     *
098     * @param message The message to display in the notification
099     */
default void logNotification(String message) {
                logNotification(message, false);
            }

    /**
105     * Send a message as a desktop notification
106     *
107     * @param message The message to display in the notification
108     * @param error   Whether to log as an error
109     */
default void logNotification(String message, boolean error) {
        if (BaritoneAPI.getSettings().desktopNotifications.value) {
            logNotificationDirect(message, error);
        }
    }

    /**
117     * Send a message as a desktop notification regardless of desktopNotifications
118     * (should only be used for critically important messages)
119     *
120     * @param message The message to display in the notification
121     */
    default void logNotificationDirect(String message) {
        logNotificationDirect(message, false);
    }

    /**
127     * Send a message as a desktop notification regardless of desktopNotifications
128     * (should only be used for critically important messages)
129     *
130     * @param message The message to display in the notification
131     * @param error   Whether to log as an error
132     */
    default void logNotificationDirect(String message, boolean error) {
                mc.addScheduledTask(() -> BaritoneAPI.getSettings().notifier.value.accept(message, error));
            }

    /**
138     * Send a message to chat only if chatDebug is on
139     *
140     * @param message The message to display in chat
141     */
    default void logDebug(String message) {
                if (!BaritoneAPI.getSettings().chatDebug.value) {
                        //System.out.println("Suppressed debug message:");
                //System.out.println(message);
                return;
                    }
            // We won't log debug chat into toasts
            // Because only a madman would want that extreme spam -_-
            logDirect(message, false);
            }

    /**
154     * Send components to chat with the [Baritone] prefix
155     *
156     * @param logAsToast Whether to log as a toast notification
157     * @param components The components to send
158     */
    default void logDirect(boolean logAsToast, ITextComponent... components) {
                ITextComponent component = new TextComponentString("");
                if (!logAsToast) {
                        // If we are not logging as a Toast
                // Append the prefix to the base component line
                component.appendSibling(getPrefix());
                        component.appendSibling(new TextComponentString(" "));
                    }
            Arrays.asList(components).forEach(component::appendSibling);
                if (logAsToast) {
                        logToast(getPrefix(), component);
                    } else {
                        mc.addScheduledTask(() -> BaritoneAPI.getSettings().logger.value.accept(component));
                    }
        }

    /**
176     * Send components to chat with the [Baritone] prefix
177     *
178     * @param components The components to send
179     */
    default void logDirect(ITextComponent... components) {
        logDirect(BaritoneAPI.getSettings().logAsToast.value, components);
            }

    /**
185     * Send a message to chat regardless of chatDebug (should only be used for critically important messages, or as a
186     * direct response to a chat command)
187     *
188     * @param message    The message to display in chat
189     * @param color      The color to print that message in
190     * @param logAsToast Whether to log as a toast notification
191     */
    default void logDirect(String message, TextFormatting color, boolean logAsToast) {
                Stream.of(message.split("\n")).forEach(line -> {
                        ITextComponent component = new TextComponentString(line.replace("\t", "    "));
                        component.getStyle().setColor(color);
                        logDirect(logAsToast, component);
                    });
            }

    /**
201     * Send a message to chat regardless of chatDebug (should only be used for critically important messages, or as a
202     * direct response to a chat command)
203     *
204     * @param message The message to display in chat
205     * @param color   The color to print that message in
206     */
    default void logDirect(String message, TextFormatting color) {
        logDirect(message, color, BaritoneAPI.getSettings().logAsToast.value);
    }

    /**
212     * Send a message to chat regardless of chatDebug (should only be used for critically important messages, or as a
213     * direct response to a chat command)
214     *
215     * @param message    The message to display in chat
216     * @param logAsToast Whether to log as a toast notification
217     */
    default void logDirect(String message, boolean logAsToast) {
        logDirect(message, TextFormatting.GRAY, logAsToast);
    }

    /**
223     * Send a message to chat regardless of chatDebug (should only be used for critically important messages, or as a
224     * direct response to a chat command)
225     *
226     * @param message The message to display in chat
     */
    default void logDirect(String message) {
        logDirect(message, BaritoneAPI.getSettings().logAsToast.value);
    }
}