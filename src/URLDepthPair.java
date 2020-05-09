public class URLDepthPair {
  String url;
  int depth;

  URLDepthPair(int depth,String url){
    this.depth = depth;
    this.url = url;
  }
  URLDepthPair(String url){
    this.url =url;
    this.depth = 0;
  }

  public int getDepth() {
    return depth;
  }

  public String getUrl() {
    return url;
  }
  public String toString(){
    return(url + " "+ depth);
  }
}