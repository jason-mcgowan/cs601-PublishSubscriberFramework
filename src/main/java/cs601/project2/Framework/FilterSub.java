package cs601.project2.Framework;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FilterSub<T> implements Subscriber<T> {

  private Predicate<T> filter;
  private Consumer<T> onFilter;

  private FilterSub() {
  }

  public FilterSub(Predicate<T> filter, Consumer<T> onFilter) {
    this.filter = filter;
    this.onFilter = onFilter;
  }

  /**
   * If the filter condition is met, sends the item to {@link #onFilter} for processing.
   *
   * @param item Item to check filter and process if true
   */
  @Override
  public void onEvent(T item) {
    if (filter.test(item)) {
      onFilter.accept(item);
    }
  }
}
