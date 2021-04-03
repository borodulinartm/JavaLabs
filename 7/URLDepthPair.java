public class URLDepthPair {
    private String url;
    private int depth;

    public URLDepthPair(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url + ' ' + depth;
    }
}
