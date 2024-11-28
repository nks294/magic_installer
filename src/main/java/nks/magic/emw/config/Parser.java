package nks.magic.emw.config;

import org.json.JSONObject;

public class Parser {

  private JSONObject obj;

  public String version;
  public String update_link;
  public String mc_version;
  public String forge_version;
  public String forge_link;
  public String modpack_link;
  public String emw_notice;

  public Parser(String data) {
    obj = new JSONObject(data);

    version = obj.getString("version");
    update_link = obj.getString("update_link");
    mc_version = obj.getString("mc_version");
    forge_version = obj.getString("forge_version");
    forge_link = obj.getString("forge_link");
    modpack_link = obj.getString("modpack_link");
    emw_notice = obj.getString("emw_notice");
  }
}
