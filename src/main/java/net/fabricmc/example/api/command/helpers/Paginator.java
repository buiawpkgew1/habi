package net.fabricmc.example.api.command.helpers;

import net.fabricmc.example.api.utils.Helper;

public class Paginator<E> implements Helper {
    public final List<E> entries;
    public int pageSize = 8;
    public int page = 1;
    public Paginator(List<E> entries) {
        this.entries = entries;
    }

    public Paginator(E... entries) {
        this.entries = Arrays.asList(entries);
    }

    public Paginator<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getMaxPage() {
        return (entries.size() - 1) / pageSize + 1;
    }

    public boolean validPage(int page) {
        return page > 0 && page <= getMaxPage();
    }

    public Paginator<E> skipPages(int pages) {
        page += pages;
        return this;
    }

    public void display(Function<E, ITextComponent> transform, String commandPrefix) {
                int offset = (page - 1) * pageSize;
                for (int i = offset; i < offset + pageSize; i++) {
                        if (i < entries.size()) {
                                logDirect(transform.apply(entries.get(i)));
                            } else {
                                logDirect("--", TextFormatting.DARK_GRAY);
                            }
            }
            boolean hasPrevPage = commandPrefix != null && validPage(page - 1);
                boolean hasNextPage = commandPrefix != null && validPage(page + 1);
                ITextComponent prevPageComponent = new TextComponentString("<<");
                if (hasPrevPage) {
                        prevPageComponent.getStyle()
                        .setClickEvent(new ClickEvent(
                                        ClickEvent.Action.RUN_COMMAND,
                            String.format("%s %d", commandPrefix, page - 1)
                    ))
                        .setHoverEvent(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                            new TextComponentString("Click to view previous page")
                        ));
                    } else {
                        prevPageComponent.getStyle().setColor(TextFormatting.DARK_GRAY);
                    }
            ITextComponent nextPageComponent = new TextComponentString(">>");
                if (hasNextPage) {
                        nextPageComponent.getStyle()
                        .setClickEvent(new ClickEvent(
                                        ClickEvent.Action.RUN_COMMAND,
                            String.format("%s %d", commandPrefix, page + 1)
                    ))
                        .setHoverEvent(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                            new TextComponentString("Click to view next page")
                        ));
                    } else {
                        nextPageComponent.getStyle().setColor(TextFormatting.DARK_GRAY);
                    }
            ITextComponent pagerComponent = new TextComponentString("");
                pagerComponent.getStyle().setColor(TextFormatting.GRAY);
                pagerComponent.appendSibling(prevPageComponent);
                pagerComponent.appendText(" | ");
                pagerComponent.appendSibling(nextPageComponent);
                pagerComponent.appendText(String.format(" %d/%d", page, getMaxPage()));
                logDirect(pagerComponent);
            }

    public void display(Function<E, ITextComponent> transform) {
                display(transform, null);
            }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Runnable pre, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
                int page = 1;
                consumer.requireMax(1);
                if (consumer.hasAny()) {
                        page = consumer.getAs(Integer.class);
                        if (!pagi.validPage(page)) {
                                throw new CommandInvalidTypeException(
                                                consumer.consumed(),
                        String.format(
                                        "a valid page (1-%d)",
                                pagi.getMaxPage()
                        ),
                        consumer.consumed().getValue()
                );
                            }
            }
            pagi.skipPages(page - pagi.page);
                if (pre != null) {
                        pre.run();
                    }
            pagi.display(transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Runnable pre, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        paginate(consumer, new Paginator<>(elems), pre, transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Runnable pre, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
                paginate(consumer, Arrays.asList(elems), pre, transform, commandPrefix);
            }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
                paginate(consumer, pagi, null, transform, commandPrefix);
            }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
                paginate(consumer, new Paginator<>(elems), null, transform, commandPrefix);
            }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
                paginate(consumer, Arrays.asList(elems), null, transform, commandPrefix);
            }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Runnable pre, Function<T, ITextComponent> transform) throws CommandException {
                paginate(consumer, pagi, pre, transform, null);
            }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Runnable pre, Function<T, ITextComponent> transform) throws CommandException {
                paginate(consumer, new Paginator<>(elems), pre, transform, null);
            }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Runnable pre, Function<T, ITextComponent> transform) throws CommandException {
                paginate(consumer, Arrays.asList(elems), pre, transform, null);
            }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Function<T, ITextComponent> transform) throws CommandException {
                paginate(consumer, pagi, null, transform, null);
            }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Function<T, ITextComponent> transform) throws CommandException {
                paginate(consumer, new Paginator<>(elems), null, transform, null);
            }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Function<T, ITextComponent> transform) throws CommandException {
                paginate(consumer, Arrays.asList(elems), null, transform, null);
            }
    }

}