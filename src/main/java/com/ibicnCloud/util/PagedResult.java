package com.ibicnCloud.util;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * 负责管理分页；
 *
 */
public class PagedResult {

    /**
     * 总数
     */
    private int recordCount = 0;

    /**
     * 每页显示的条数
     */
    private int pageSize = 25;

    private int defaultPageSize = 25;

    /**
     * 总页数
     */
    private int pageCount = 0;

    /**
     * 当前页数
     */
    private int currentPage = 1;

    private int start = 0;

    /**
     * 分页中的参数
     */
    private String parameter = "";

    /**
     * 返回的结果集
     */
    private Vector result = new Vector();

    /**
     *
     */
    public PagedResult() {
        super();
    }

    /**
     * 初始化分页类
     *
     * @param recordCount
     *            总条数
     * @param currentPage
     *            当前页
     * @param pageSize
     *            每页条数
     */
    public PagedResult(int recordCount, int currentPage, int pageSize) {
        if (recordCount < 0)
            recordCount = 0;
        this.recordCount = recordCount;

        if (pageSize < 1)
            pageSize = defaultPageSize;
        this.pageSize = pageSize;

        int pageCount = recordCount / pageSize;
        if ((recordCount - pageCount * pageSize) > 0)
            pageCount++;
        this.pageCount = pageCount;

        if (currentPage < 1)
            currentPage = 1;
        else if (currentPage > this.pageCount)
            currentPage = this.pageCount;
        this.currentPage = currentPage;
        if (this.currentPage < 1)
            this.currentPage = 1;
        this.start = (currentPage - 1) * pageSize;
    }

    /**
     * 默认分页代码
     *
     * @return String
     */
    public String pagination() {
        return glPagination(null, null);
    }


    /**
     * 分页代码
     *
     * @param parameter
     *            搜索信息
     * @param linkColor
     *            有链接代码
     * @param unlinkColor
     *            无链接代码
     * @return
     */
    public String pagination(String linkColor, String unlinkColor) {
        if (!StringUtil.isEmpty(parameter))
            parameter += "&";
        else
            parameter = "";

        if (StringUtil.isEmpty(linkColor))
            linkColor = "#FFFFFF";

        if (StringUtil.isEmpty(unlinkColor))
            unlinkColor = "#CCCCCC";

        StringBuffer sb = new StringBuffer();

        sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        sb.append("<tr><td>&nbsp;");
        sb.append("页次：<font color=\"#FF0000\">" + currentPage + "</font>");
        sb.append("<strong>/</strong><font color=\"#FF0000\">" + pageCount + "</font>");
        sb.append("</td><td align=\"right\">");
        if (currentPage <= 1) {
            sb.append("<font color=\"" + unlinkColor + "\">首页&nbsp;|&nbsp;上一页&nbsp;|&nbsp;</font>");
        } else {
            sb.append("<a href=\"?" + parameter + "cp=1\"><font color=\"" + linkColor + "\">首页</font></a>&nbsp;|&nbsp;");
            sb.append("<a href=\"?" + parameter + "cp=" + (currentPage - 1) + "\"><font color=\"" + linkColor + "\">上一页</font></a>&nbsp;|&nbsp;");
        }
        if (currentPage == pageCount || pageCount == 0) {
            sb.append("<font color=\"" + unlinkColor + "\">下一页&nbsp;|&nbsp;尾页&nbsp;</font>");
        } else {
            sb.append("<a href=\"?" + parameter + "cp=" + (currentPage + 1) + "\"><font color=\"" + linkColor + "\">下一页</font></a>&nbsp;|&nbsp;");
            sb.append("<a href=\"?" + parameter + "cp=" + pageCount + "\"><font color=\"" + linkColor + "\">尾页</font></a>&nbsp;");
        }
        sb.append("&nbsp;每页<font color=\"#FF0000\">" + pageSize + "</font>条，");
        sb.append("共<font color=\"#FF0000\">" + recordCount + "</font>条&nbsp;");
        if (pageCount > 1) {
            sb.append("&nbsp;转到<select style=\"width:40px\" onChange=\"javascript:document.location='?" + parameter + "cp='+this.value\">");
            for (int i = 1; i <= pageCount; i++) {
                sb.append("<option value=\"" + i + "\"");
                if (i == currentPage)
                    sb.append(" selected");
                sb.append(">" + i + "</option>");
            }
            sb.append("</select>页");
            // sb.append("&nbsp;转到<input id=\"cp\" name=\"cp\" type=\"text\"
            // size=\"2\" value=\""+ currentPage +"\" ");
            // sb.append(" onclick=\"window.open('?"+ parameter
            // +"'+this.value,'_self','')\">页");
        }
        sb.append("</td></tr>");
        sb.append("</table>");

        return sb.toString();
    }

    /**
     * 共有152条，以上是第2-20条.          首页 | 上一页 | 下一页 | 尾页  转到  *页
     *
     * @param parameter
     *            搜索信息
     * @param linkColor
     *            有链接代码
     * @param unlinkColor
     *            无链接代码
     * @return
     */

    public String glPagination(String linkColor, String unlinkColor) {
        if (!StringUtil.isEmpty(parameter))
            parameter += "&";
        else
            parameter = "";

        if (StringUtil.isEmpty(linkColor))
            linkColor = "#555555";

        if (StringUtil.isEmpty(unlinkColor))
            unlinkColor = "#CCCCCC";

        int beginSize = ((currentPage-1)*pageSize+1);
        int endSize = ((currentPage-1)*pageSize+CollectionUtil.size(result)); //结束数量
        StringBuffer sb = new StringBuffer();
        sb.append("<div class=\"gonggao_footer\">");
        if(recordCount == 0){
            sb.append("<span style=\"float: right\">没有找到符合条件的记录！</span>");
        }else{
            sb.append("<span>共有<font color=\"#FF0000\">" +recordCount + "</font>条，以上是第<font color=\"#FF0000\">" + beginSize + "</font>-<font color=\"#FF0000\">" + endSize + "</font>条.</span>");
            sb.append("<div class=\"page\"><div>");
            if (getCurrentPage() <= 1) {
                sb.append("<font color=\"" + unlinkColor + "\">首页&nbsp;|&nbsp;上一页&nbsp;|&nbsp;</font>");
            }else{
                sb.append("<a href=\"?" + parameter + "cp=1\"><font color=\"" + linkColor + "\">首页</font></a>&nbsp;|&nbsp;");
                sb.append("<a href=\"?" + parameter + "cp=" + (currentPage - 1) + "\"><font color=\"" + linkColor + "\">上一页</font></a>&nbsp;|&nbsp;");
            }
            if (getCurrentPage() == pageCount || pageCount == 0) {
                sb.append("<font color=\"" + unlinkColor + "\">下一页&nbsp;|&nbsp;尾页&nbsp;</font>");
            } else {
                sb.append("<a href=\"?" + parameter + "cp=" + (currentPage + 1) + "\"><font color=\"" + linkColor + "\">下一页</font></a>&nbsp;|&nbsp;");
                sb.append("<a href=\"?" + parameter + "cp=" + pageCount + "\"><font color=\"" + linkColor + "\">尾页</font></a>&nbsp;");
            }
            if (getPageCount() > 1) {
                sb.append("&nbsp;转到<select style=\"width:40px\" onChange=\"javascript:document.location='?" + parameter + "cp='+this.value\">");
                for (int i = 1; i <= pageCount; i++) {
                    sb.append("<option value=\"" + i + "\"");
                    if (i == currentPage)
                        sb.append(" selected");
                    sb.append(">" + i + "</option>");
                }
                sb.append("</select>页");
            }
            sb.append("</div></div>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    public String pagination2(String linkColor, String unlinkColor) {
        if (!StringUtil.isEmpty(parameter))
            parameter += "&";
        else
            parameter = "";

        if (StringUtil.isEmpty(linkColor))
            linkColor = "#FFFFFF";

        if (StringUtil.isEmpty(unlinkColor))
            unlinkColor = "#CCCCCC";

        StringBuffer sb = new StringBuffer();

        sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        sb.append("<tr><td class=writh>&nbsp;");
        sb.append("页次：<font color=\"#FF0000\">" + currentPage + "</font>");
        sb.append("<strong>/</strong><font color=\"#FF0000\">" + pageCount + "</font>");
        sb.append("</td><td class=writh align=\"right\">");
        if (currentPage <= 1) {
            sb.append("<font color=\"" + unlinkColor + "\">首页&nbsp;|&nbsp;上一页&nbsp;|&nbsp;</font>");
        } else {
            sb.append("<a href=\"?" + parameter + "cp=1\"><font color=\"" + linkColor + "\">首页</font></a>&nbsp;|&nbsp;");
            sb.append("<a href=\"?" + parameter + "cp=" + (currentPage - 1) + "\"><font color=\"" + linkColor + "\">上一页</font></a>&nbsp;|&nbsp;");
        }
        if (currentPage == pageCount || pageCount == 0) {
            sb.append("<font color=\"" + unlinkColor + "\">下一页&nbsp;|&nbsp;尾页&nbsp;</font>");
        } else {
            sb.append("<a href=\"?" + parameter + "cp=" + (currentPage + 1) + "\"><font color=\"" + linkColor + "\">下一页</font></a>&nbsp;|&nbsp;");
            sb.append("<a href=\"?" + parameter + "cp=" + pageCount + "\"><font color=\"" + linkColor + "\">尾页</font></a>&nbsp;");
        }
        sb.append("&nbsp;每页<font color=\"#FF0000\">" + pageSize + "</font>条，");
        sb.append("共<font color=\"#FF0000\">" + recordCount + "</font>条&nbsp;");
        if (pageCount > 1) {
            sb.append("&nbsp;转到<select style=\"width:40px\" onChange=\"javascript:document.location='?" + parameter + "cp='+this.value\">");
            for (int i = 1; i <= pageCount; i++) {
                sb.append("<option value=\"" + i + "\"");
                if (i == currentPage)
                    sb.append(" selected");
                sb.append(">" + i + "</option>");
            }
            sb.append("</select>页");
            // sb.append("&nbsp;转到<input id=\"cp\" name=\"cp\" type=\"text\"
            // size=\"2\" value=\""+ currentPage +"\" ");
            // sb.append(" onclick=\"window.open('?"+ parameter
            // +"'+this.value,'_self','')\">页");
        }
        sb.append("</td></tr>");
        sb.append("</table>");

        return sb.toString();
    }

    /**
     * 【首页】 【上一页】 1 2 3 4 【下一页】 【尾页】
     *
     * @param parameter
     * @return
     */
    public String pagination_site(String parameter) {
        return pagination_site(parameter, 3);
    }

    /**
     * 【首页】 【上一页】 1 2 3 4 【下一页】 【尾页】
     *
     * @param parameter
     * @param range
     * @return
     */
    public String pagination_site(String parameter, int range) {
        if (!StringUtil.isEmpty(parameter))
            parameter += "&";
        else
            parameter = "";

        StringBuffer sb = new StringBuffer();

        sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        sb.append("<tr><td align=\"center\">");
        if (currentPage <= 1) {
            sb.append("【首页】&nbsp;");
            sb.append("【上一页】&nbsp;");
        } else {
            sb.append("<a href=\"?" + parameter + "cp=1\" class='text3'>");
            sb.append("【首页】</a>&nbsp;");
            sb.append("<a href=\"?" + parameter + "cp=" + (currentPage - 1) + "\" class='text3'>");
            sb.append("【上一页】</a>&nbsp;");
        }

        int start = Math.max((currentPage - range), 1);
        int end = Math.min((currentPage + range), pageCount);
        for (int i = start; i <= end; i++) {
            if (i == currentPage)
                sb.append("<a href=\"?" + parameter + "cp=" + i + "\" class='text3'>&nbsp;<b>" + i + "</b>&nbsp;</a>");
            else
                sb.append("<a href=\"?" + parameter + "cp=" + i + "\" class='text3'>&nbsp;" + i + "&nbsp;</a>");
        }

        if (currentPage == pageCount || pageCount == 0) {
            sb.append("【下一页】&nbsp;");
            sb.append("【尾页】");
        } else {
            sb.append("<a href=\"?" + parameter + "cp=" + (currentPage + 1) + "\" class='text3'>");
            sb.append("【下一页】</a>&nbsp;");
            sb.append("<a href=\"?" + parameter + "cp=" + pageCount + "\" class='text3'>");
            sb.append("【尾页】</a> 共");
            sb.append(recordCount);
            sb.append("条");

        }
        sb.append("</td></tr>");
        sb.append("</table>");

        return sb.toString();
    }

    public String pagination_site(HttpServletRequest request, String parameter, int range) {
        String selfurl = request.getRequestURI();
        if (!StringUtil.isEmpty(parameter))
            parameter += "&";
        else
            parameter = "";

        StringBuffer sb = new StringBuffer();

        sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        sb.append("<tr><td align=\"center\">");
        if (currentPage <= 1) {
            sb.append("<img src=\"/resource/images/common/btn_first.gif\">&nbsp;");
            sb.append("<img src=\"/resource/images/common/btn_previous.gif\">&nbsp;");
        } else {
            sb.append("<a href=\"?" + selfurl + "?" + parameter + "cp=1\" class='black'>");
            sb.append("<img src=\"/resource/images/common/btn_first.gif\" border=\"0\"></a>&nbsp;");
            sb.append("<a href=\"?" + selfurl + "?" + parameter + "cp=" + (currentPage - 1) + "\" class='black'>");
            sb.append("<img src=\"/resource/images/common/btn_previous.gif\" border=\"0\"></a>&nbsp;");
        }

        int start = Math.max((currentPage - range), 1);
        int end = Math.min((currentPage + range), pageCount);
        for (int i = start; i <= end; i++) {
            if (i == currentPage)
                sb.append("<a href=\"?" + selfurl + "?" + parameter + "cp=" + i + "\" class='black'>&nbsp;<b>" + i + "</b>&nbsp;</a>");
            else
                sb.append("<a href=\"?" + selfurl + "?" + parameter + "cp=" + i + "\" class='black'>&nbsp;" + i + "&nbsp;</a>");
        }

        if (currentPage == pageCount || pageCount == 0) {
            sb.append("<img src=\"/resource/images/common/btn_next.gif\">&nbsp;");
            sb.append("<img src=\"/resource/images/common/btn_end.gif\">");
        } else {
            sb.append("<a href=\"?" + selfurl + "?" + parameter + "cp=" + (currentPage + 1) + "\" class='black'>");
            sb.append("<img src=\"/resource/images/common/btn_next.gif\" border=\"0\"></a>&nbsp;");
            sb.append("<a href=\"?" + selfurl + "?" + parameter + "cp=" + pageCount + "\" class='black'>");
            sb.append("<img src=\"/resource/images/common/btn_end.gif\" border=\"0\"></a>");
        }
        sb.append("</td></tr>");
        sb.append("</table>");

        return sb.toString();
    }

    public int getStart() {
        return this.start;
    }

    /**
     * 是否为尾页；
     */
    public boolean isEndPage() {
        return currentPage == pageCount || pageCount == 0;
    }

    /**
     * 是否为首页；
     */
    public boolean isStartPage() {
        return currentPage == 0;
    }

    /**
     * @return Returns the currentPage.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage
     *            The currentPage to set.
     */
    public void setCurrentPage(int currentPage) {
        if (currentPage < 1)
            currentPage = 1;
        this.currentPage = currentPage;
    }

    /**
     * @return Returns the pageCount.
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * @param pageCount
     *            The pageCount to set.
     */
    public void setPageCount(int pageCount) {
        if (pageCount < 0)
            pageCount = 0;
        this.pageCount = pageCount;
    }

    /**
     * @return Returns the pageSize.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     *            The pageSize to set.
     */
    public void setPageSize(int pageSize) {
        if (pageSize < 0)
            pageSize = defaultPageSize;
        this.pageSize = pageSize;
    }

    /**
     * @return Returns the recordCount.
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * @param recordCount
     *            The recordCount to set.
     */
    public void setRecordCount(int recordCount) {
        if (recordCount < 0)
            recordCount = 0;
        this.recordCount = recordCount;
    }

    /**
     * @return Returns the result.
     */
    public Vector getResult() {
        return result;
    }

    /**
     * @param result
     *            The result to set.
     */
    public void setResult(Vector result) {
        this.result = result;
    }

    public static void main(String[] args) {
        PagedResult re = new PagedResult(85, 1, 10);
        System.out.println(re.pagination_site("wnang=88", 4));
    }

    public String getParameter() {
        return parameter;
    }

    public PagedResult setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

}