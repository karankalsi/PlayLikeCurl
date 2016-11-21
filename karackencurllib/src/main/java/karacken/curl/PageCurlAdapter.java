package karacken.curl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karacken on 18/11/16.
 */
public class PageCurlAdapter  {

    List<String> res_list = new ArrayList<>();
    public PageCurlAdapter(String[] res_list)
    {
        for(String res_item : res_list)
        {
            this.res_list.add(res_item);
        }
    }

    public int getCount()
    {
        return res_list.size();
    }

    public String getItemResource(int position)
    {
        return res_list.get(position);
    }


}
