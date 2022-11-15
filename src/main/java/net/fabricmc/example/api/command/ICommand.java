package net.fabricmc.example.api.command;

import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.utils.Helper;

import java.util.List;
import java.util.stream.Stream;

public interface ICommand extends Helper {

    /**
    * 执行此命令时调用.
     */
    void execute(String label, IArgConsumer args) throws CommandException;

    /**
    * 当命令需要tab键完成时调用。返回表示要放入完成列表的条目的流。
     */
    Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException;

    /**
    * @return A <b>single-line</b> 包含此命令用途的简短描述的字符串.
     */
    String getShortDesc();
    /**
     *  @return 当用户希望查看时，返回将由help命令打印的行列表。
     */
    List<String> getLongDesc();

    /**
     * @return 返回一个名称列表，该列表允许将参数传递给该命令
     */
    List<String> getNames();

    /**
     * @return {@code true}如果该命令应从“帮助”菜单中隐藏
     */
    default boolean hiddenFromHelp() {
        return false;
    }
}
