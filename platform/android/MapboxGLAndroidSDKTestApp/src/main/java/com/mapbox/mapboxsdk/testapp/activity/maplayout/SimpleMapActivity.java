package com.mapbox.mapboxsdk.testapp.activity.maplayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.mapboxsdk.style.sources.VectorSource;
import com.mapbox.mapboxsdk.testapp.R;
import com.mapbox.mapboxsdk.testapp.utils.NavUtils;

import java.io.File;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOutlineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Test activity showcasing a simple MapView without any MapboxMap interaction.
 */
public class SimpleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

  private MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_simple);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // activity uses singleInstance for testing purposes
        // code below provides a default navigation when using the app
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    // activity uses singleInstance for testing purposes
    // code below provides a default navigation when using the app
    NavUtils.navigateHome(this);
  }

  @Override
  public void onMapReady(@NonNull MapboxMap mapboxMap) {


      LatLng lb = new LatLng(36.026691, 115.305265);
      LatLng lt = new LatLng(36.175867, 115.305265);
      LatLng rt = new LatLng(36.175867, 115.481908);
      LatLng rb = new LatLng(36.026691, 115.481908);
      LatLngBounds latLngBounds = new LatLngBounds.Builder()//根据坐标集，初始化一个坐标界限
              .include(lb)
              .include(lt)
              .include(rt)
              .include(rb)
              .build();
      mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,10),2000);

      /*LatLng center = new LatLng(34.518923, 101.566284);
      mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10),2000);*/


      final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    // 加载本地指定的style.json文件
    String styleUri = Style.LIGHT;
    String localStyleFilePath = rootPath + "/style.json";
    File styleFile = new File(localStyleFilePath);
    if (styleFile.exists()){
      styleUri = Style.PROTOCOL_FILE + localStyleFilePath;
    }
    mapboxMap.setStyle(styleUri, new Style.OnStyleLoaded() {
      @Override
      public void onStyleLoaded(@NonNull Style style) {

          String token = "4fa4968c451a14a7ede2b6de084c38b630b2404ab8b0365f75d1f237aac3baab61f3a9937c6443cc248cce81f8f2a27b4131c539f86682da41e58aacf01d82f92fb6508b283e0ec65ca823ba65fae1a4fe7815fc9c630d421535f132200afeecd1bd89a24cc95048f5a3fd1930dcfeb84401244cca4214d1b232fe07b6c7d4a6";
          String pbfUrl = "https://sjy-api.gagogroup.cn/api/v1/country/{z}/{x}/{y}.pbf?token="+token;

          String vtMbtilesFilePath = rootPath + "/dt_zzdk.mbtiles";
          //String vtMbtilesFilePath = rootPath + "/countries.mbtiles";

          File vtMbtiles = new File(vtMbtilesFilePath);
          if(vtMbtiles.exists()){

              // 天地图影像注记
              TileSet vecTileSet1 = new TileSet("1.0.0", "http://t0.tianditu.gov.cn/DataServer?T=img_w&X={x}&Y={y}&L={z}&tk=b7faf4433012e536e815a6d391cefb3c");
              RasterSource vecRasterSource1 = new RasterSource("TDT_CIA_SOURCEID", vecTileSet1, 128);
              style.addSource(vecRasterSource1);
              RasterLayer vecRasterLayer1 = new RasterLayer("TDT_CIA_LAYERID", "TDT_CIA_SOURCEID");
              style.addLayer(vecRasterLayer1);

              String mbtileUrl = "mbtiles://" + vtMbtilesFilePath;
              TileSet vecTileSet = new TileSet("1.0.0", mbtileUrl);
              vecTileSet.setScheme("tms");
              VectorSource vecSource = new VectorSource("vtMbtilesSourceTest", vecTileSet);
              style.addSource(vecSource);
              FillLayer terrainData = new FillLayer("vtMbtilesLayerTest", "vtMbtilesSourceTest");
              terrainData.setSourceLayer("dt_zzdk"); //sanjiangyuan, country dt_zzdk
              terrainData.setProperties(
                    fillColor(Color.parseColor("#ff69b4")),
                      fillOutlineColor(Color.parseColor("#f2b648")),
                      lineWidth(2.0f)
              );
              style.addLayer(terrainData);
          }


        // 加载本地指定的mbtiles文件
        /*String mbtilesFilePath = rootPath + "/GEOTIFF.mbtiles";
        File mbtiles = new File(mbtilesFilePath);
        if (mbtiles.exists()) {


          TileSet vecTileSet2 = new TileSet("1.0.0", "http://t0.tianditu.gov.cn/DataServer?T=vec_w&X={x}&Y={y}&L={z}&tk=b7faf4433012e536e815a6d391cefb3c");
          RasterSource vecRasterSource2 = new RasterSource("TDT_VEC_SOURCEID", vecTileSet2, 128);
          style.addSource(vecRasterSource2);
          RasterLayer vecRasterLayer2 = new RasterLayer("TDT_VEC_LAYERID", "TDT_VEC_SOURCEID");
          style.addLayer(vecRasterLayer2);

          TileSet vecTileSet = new TileSet("1.0.0", "http://t0.tianditu.gov.cn/DataServer?T=cia_w&X={x}&Y={y}&L={z}&tk=b7faf4433012e536e815a6d391cefb3c");
          RasterSource vecRasterSource = new RasterSource("TDT_CIA_SOURCEID", vecTileSet, 128);
          style.addSource(vecRasterSource);
          RasterLayer vecRasterLayer = new RasterLayer("TDT_CIA_LAYERID", "TDT_CIA_SOURCEID");
          style.addLayer(vecRasterLayer);

          TileSet vecTileSet1 = new TileSet("1.0.0", Source.PROTOCOL_MBTILES+mbtilesFilePath);
          RasterSource vecRasterSource1 = new RasterSource("mbtilesSourceTest", vecTileSet1, 128);
          style.addSource(vecRasterSource1);
          RasterLayer vecRasterLayer1 = new RasterLayer("mbtilesLayerTest", "mbtilesSourceTest");
          style.addLayer(vecRasterLayer1);


        }*/

        // load ArcGIS10.2 bundle tiles

      }
    });


  }
}
