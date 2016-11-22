![demo](demo.gif)

Introduction
============

It is an project for implementing <b>'Page Curl'</b> similar to what we see in Google Play Books using OpenGL 1.0.
The source code is released under <b>MIT License</b> can be used in commercial or personal projects.

The main motive to work on <b>'PlayLikeCurl'</b> is that its way more efficient than the traditional curl effect and is
better looking because it's way more smoother than the traditional curl.

In this project the below common sinusoidal graph equation is used to achieve the play like curl:-<br/><br/>
<b>Asin(2π/λ*x)</b><br /> 

Where,<br /> 
<b>A</b> = Amplitude (i.e. the elevation of curl we want).<br /> 
<b>λ</b> = Wavelength (i.e. the length of the curl we want).<br /> 
<b>x</b> = The X axis variable which will change as you move the page.<br /> 

In this i have drawn 3 pages on SurfaceView namely <b>'LeftPage'</b> , <b>'CenterPage'</b> and <b>'RightPage'</b>,
<b>'CenterPage'</b> is always visible and is responsible for 'right curl' animation while <b>'LeftPage'</b> is responsible
for 'left curl' animation and 'RightPage' just stay static.<br /> 

For detecting gestures the default GestureDetector class has been used.

ToDo
====
* Need to add shadow below the page when it will move.



How to Use
======================

Add Gradle dependency:

```xml
dependencies {
    compile 'com.github.karacken:karackencurllib:0.0.1'
}
```

Use Code:

```java
PageSurfaceView  pageSurfaceView = new PageSurfaceView(this);
String[] asset_res_array=null;
asset_res_array=  new String[]{"page1.png", "page2.png", "page3.png"};
PageCurlAdapter pageCurlAdapter=new PageCurlAdapter(asset_res_array);
pageSurfaceView.setPageCurlAdapter(pageCurlAdapter);
setContentView(pageSurfaceView);
```



References
======================
* Harism Page Curl [https://github.com/harism/android_page_curl]
* WikiPedia [https://en.wikipedia.org/wiki/Sine]
* OpenGL Tutorials By Google [https://developer.android.com/guide/topics/graphics/opengl.html]
