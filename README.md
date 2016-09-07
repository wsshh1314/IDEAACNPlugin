# IDEAACNPlugin

This is an IntelliJIDEA/Android Studio Plugin <br>

* Append Class Name at each end of the declared var name(s) <br>
* 声明的变量名称后追加类型词尾，避免（尤其是Android开发中）声明的view变量名带大量类型简写的丑陋规则


##Change Notes
* v0.3 ,泛型类型的变量名称词尾追加错误修正
* v0.2 ,增加行内连续声明变量追加词尾
* v0.1 ,变量名追加类型词尾

eg1:
<p>
<code>
private static ListView _test
</code><br>
then press shortcut key (default is "ALT+C"),you will get<br>
<code>
private static ListView _testListView
</code> </p>
eg2:
<p>
<code>
private  Map&lt;String,Object&gt; aa,bb,cc;
</code>
<br>
press shortcut key (default is "ALT+ B"),then you will get <br>
<code>
private  Map&lt;String,Object&gt; aaMap,bbMap,ccMap;
</code>
</p>


