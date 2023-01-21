package commands;

import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import duke.Duke;

public abstract class Command implements BiFunction<String[], Duke, Stream<String>> {
  private final String label;
  private Builder<String> streamBuilder;

  public Command(String label) {
    this.label = label;
  }

  protected void output(String str) {
    streamBuilder.accept(str);
  } 

  protected void output(String formatStr, Object ...args) {
    output(String.format(formatStr, args));
  }

  public String getLabel() { return label; }

  protected abstract void execute(String[] tokens, final Duke instance);

  public final Stream<String> apply(String[] tokens, final Duke instance) {
    this.streamBuilder = Stream.builder();
    execute(tokens, instance);
    return streamBuilder.build();
  }

}