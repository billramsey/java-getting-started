import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.HTMLElements;


public class HTMLTagParse {

  private HashMap<String, Integer> tagCount = new HashMap<String, Integer>();
  private HashSet<String> TAGS;
  
  public HTMLTagParse() {
    TAGS = new HashSet<String>(HTMLElements.getElementNames());
    //add custom tags here
    //TAGS.add("mytag");
  }
  
  
  public HashMap<String, Integer> getTagCount() {
    return tagCount;
  }
  public void addTag(String tag) {
    if (tagCount.containsKey(tag)) {
      int count = tagCount.get(tag);
      tagCount.put(tag, count + 1);
    } else {
      tagCount.put(tag, 1);
    }
  }
  //http://blog.codinghorror.com/parsing-html-the-cthulhu-way/
  //old way.  Too many problems.
  public String replaceTags(CharSequence htmlString) {
   // Pattern patt = Pattern.compile("<b>([^<]*)</b>");
    Pattern patt = Pattern.compile("(<(.*?)([' '>]))");
    Matcher m = patt.matcher(htmlString);
    StringBuffer sb = new StringBuffer(htmlString.length());
    while (m.find()) {
      
      String text = m.group(1);
      //System.out.println("text1: " + text);
      String text2 = m.group(2);
      text2 = text2.replace("<", "&lt;");
      text2 = text2.replace(">", "&gt;");
      String text3 = m.group(3);
      text3 = text3.replace(">", "&gt;");
      //
      String tag = text2;
      //System.out.println("found: " + tag);
      if (tag.length() > 0 && tag.charAt(0) == '/') {
        tag = text2.substring(1, text2.length());
      }
      if (TAGS.contains(tag.toLowerCase())) {
        if (tagCount.containsKey(tag)) {
          int count = tagCount.get(tag);
          tagCount.put(tag, count + 1);
        } else {
          tagCount.put(tag, 1);
        }
        System.out.println("Found tag: " + tag);
        m.appendReplacement(sb, "&lt;<span class=\"tag_toggle_" + tag + "\">" 
            + Matcher.quoteReplacement(text2) 
            +  "</span>" + Matcher.quoteReplacement(text3));
      } else {
        m.find(m.start());
        System.out.println("could not find tag: " + tag);
        m.appendReplacement(sb, "&lt;" + Matcher.quoteReplacement(text2) 
            + Matcher.quoteReplacement(text3));
      }
    }
    m.appendTail(sb);
    return sb.toString();
  }
  
  public String replaceTagsString(String htmlString) {

    StringBuilder sb = new StringBuilder();
    int i = 0;
    int totalLength = htmlString.length();
    while (i < totalLength)  {
      char c = htmlString.charAt(i);
      if (c == '<') {
        //System.out.println("c: " + c);
        //append the escaped <
        sb.append("&lt;");
        
        //advance on a /closing tag
        if (i+1 < totalLength && htmlString.charAt(i+1) == '/') {
          sb.append('/');
          i++;
        }
        
        //See if the tag after < makes sense.  advance until a space or a >
        int j = i + 1;
        StringBuilder tag = new StringBuilder();
        while (j < totalLength) {
          char subchar = htmlString.charAt(j);
          if (subchar == ' ' || subchar == '>') {
            break;
          }
          tag.append(subchar);
          j++;
        }
        
        //See if we found a tag.  Add it to the output
        if (TAGS.contains(tag.toString().toLowerCase())) {
          String cleanTag = tag.toString().toLowerCase();
          addTag(cleanTag);
          sb.append("<span class=\"tag_toggle_" + cleanTag + "\">" 
              + tag.toString() + "</span>");
          //advance to j - 1 (remember we incr below)
          i = j - 1;
        } else {
          //Do nothing. We found no tag, and it could be overlapping 
          //like <=4<script.
          //so we just go on to the next character.
          //System.out.println("Continuing");
        }
      } else if (c == '>') {
        //escape <
        sb.append("&gt;");
      } else {
        sb.append(c);
      }
      
      i++;
    }

    
    return sb.toString();
  }

  
  
  
  
  public static void main(String[] args) {
    String foo = "<html><script>5<=4</script><body> blah <input type =" 
        + "\"submit\">goes here <\"==a.charAt(0)?a.replace(/<\\w+/,\" $&"
        + "</body></html><";
    HTMLTagParse hp = new HTMLTagParse();
    //System.out.println(hp.replaceTags(foo));
    System.out.println("old:");
    System.out.println(foo);
    System.out.println("new");
    System.out.println(hp.replaceTagsString(foo));
    
    /*
     HashMap<String, Integer> tags = hp.getTagCount();
    for (String tag: tags.keySet()) {
      System.out.println(tag + " : " + tags.get(tag));
    }
    */
    String b = "g \n t";
    System.out.println(hp.replaceTagsString(b));
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < b.length(); i++) {
      sb.append(b.charAt(i));
    }
    System.out.println("b:  " + b);
    System.out.println("sb: " + sb.toString());
    
    HashSet<String> TAGS = new HashSet<String>(HTMLElements.getElementNames());
    //  TAGS.add("!doctype");

    System.out.println(TAGS.contains("!DOCTYPE"));
    
  }
}
