package cs601.project2;

import java.util.Arrays;

/**
 * Houses the data for Review records from the Amazon Cell Phone review dataset available from <a
 * href="http://snap.stanford.edu/data/amazon/productGraph/categoryFiles/reviews_Cell_Phones_and_Accessories_5.json.gz">
 * snap.stanford.edu/data/amazon/productGraph/categoryFiles/
 * reviews_Cell_Phones_and_Accessories_5.json.gz</a>.
 *
 * @author Jason McGowan
 */
public class Review {
  private String reviewerID;
  private String asin;
  private String reviewerName;
  private int[] helpful;
  private String reviewText;
  private double overall;
  private String summary;
  private int unixReviewTime;
  private String reviewTime;

  public String getAsin() {
    return asin;
  }

  @Override
  public String toString() {
    return "Review{"
        + "reviewerId='"
        + reviewerID
        + '\''
        + ", asin='"
        + asin
        + '\''
        + ", reviewerName='"
        + reviewerName
        + '\''
        + ", helpful="
        + Arrays.toString(helpful)
        + ", reviewText='"
        + reviewText
        + '\''
        + ", overall="
        + overall
        + ", summary='"
        + summary
        + '\''
        + ", unixReviewTime="
        + unixReviewTime
        + ", reviewTime='"
        + reviewTime
        + '\''
        + '}';
  }

  public String getReviewerID() {
    return reviewerID;
  }

  public String getReviewerName() {
    return reviewerName;
  }

  public int[] getHelpful() {
    return helpful;
  }

  public String getReviewText() {
    return reviewText;
  }

  public double getOverall() {
    return overall;
  }

  public String getSummary() {
    return summary;
  }

  public int getUnixReviewTime() {
    return unixReviewTime;
  }

  public String getReviewTime() {
    return reviewTime;
  }
}
