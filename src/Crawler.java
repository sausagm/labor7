import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Crawler {
  public static final String URL_PREFIX = "https://";
  int max_depth = 1;

  Crawler(String host) throws IOException {
    Socket soc = new Socket(host, 80);
    URLDepthPair hm = new URLDepthPair("https://" + host + "/");
    String host1 = host;
    LinkedList<URLDepthPair> viewed_url = new LinkedList<>();
    LinkedList<URLDepthPair> not_viewed_url = new LinkedList<>();
    not_viewed_url.add(hm);
    soc.setSoTimeout(2000);
    while ((!not_viewed_url.isEmpty())) {
      try {
        URL url = new URL(not_viewed_url.getFirst().getUrl());
        try {
          LineNumberReader reader = new LineNumberReader(new InputStreamReader(url.openStream()));
          String string = reader.readLine();
          while (string != null) {
            for (String reta  : string.split("href=\""))
              try {
                if (string.contains("href=\"" + URL_PREFIX) & reta.startsWith(URL_PREFIX)) {
                  hm = new URLDepthPair((reta.substring(0, reta.indexOf("\"")).split("/").length - 3), reta.substring(0, reta.indexOf("\"")));
                  if (hm.getDepth()<=max_depth & hm.getUrl().contains(host1)) {
                    int sizea = not_viewed_url.size();
                    int sizeb = viewed_url.size();
                    boolean aState=false;
                    boolean bState=false;
                    int i=0;
                    int j=0;
                    while ((!aState & i<sizea)|(!bState & i<sizeb)){
                      if (i<sizea) {
                        if (not_viewed_url.get(i).getUrl().contains(hm.getUrl())) {
                          aState = true;
                        }
                        i++;
                      }
                      if (j<sizeb){
                        if (viewed_url.get(j).getUrl().contains(hm.getUrl())) {
                          bState = true;
                        }
                        j++;
                      }
                    }
                    if (!aState & !bState) {
                      not_viewed_url.add(hm);
                    }
                  }
                }
              } catch (StringIndexOutOfBoundsException e) {
              }

            string = reader.readLine();
          }
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

      } catch (MalformedURLException ex) {
        ex.printStackTrace();
      }
      soc.close();
      viewed_url.add(not_viewed_url.getFirst());
      not_viewed_url.removeFirst();
      System.out.println("Проверенная cсылка: " + viewed_url.getLast().getUrl());
      System.out.println("Ссылок проверено: " + viewed_url.size());
    }
    viewed_url.sort(new Comparator<URLDepthPair>() {
      @Override
      public int compare(URLDepthPair o1, URLDepthPair o2) {
        return o1.getUrl().compareTo(o2.getUrl());
      }
    });
    Set<URLDepthPair> list = new HashSet<URLDepthPair>(viewed_url);
    viewed_url.clear();
    viewed_url.addAll(list);
    System.out.println(viewed_url);
  }

  public static void main(String args[]) throws Exception {
    new Crawler("yandex.ru");
  }
}
