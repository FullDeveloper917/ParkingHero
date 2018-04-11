package com.emraz.parkinghero.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import com.emraz.parkinghero.R;

/**
 * Created by Yousuf on 7/8/2017.
 */

public class AlertDialogSingle {

  private Context mContext;
  private String title = "";
  private String message = "";
  private int[] iconIds;
  String [] names;
  private String positiveBtnText = "OK";
  private String negativeBtnText = "Cancel";



  AlertDialog.Builder mBuilder;

  public interface OnSelectListener {
    public void onSelect(int index);
  }

  OnSelectListener mOnSelectListener;

  public AlertDialogSingle(Context mContext, String title, String message, int[] iconIds, String [] names) {
    this.mContext = mContext;
    this.title = title;
    this.message = message;

    makeDialog();
  }

  public void setOnSelectListener( OnSelectListener mListener ){
    mOnSelectListener = mListener;
  }


  private void makeDialog(){
    if( mContext != null ){

      mBuilder = new AlertDialog.Builder(mContext);
     // mBuilder.setTitle(title);
    //  mBuilder.setMessage(message);

      ///View rootView = LayoutInflater.from(mContext).inflate(R.)

      mBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          if (mOnSelectListener != null) {
          //  mOnSelectListener.onOkSelect();
          }

          dialogInterface.dismiss();
        }
      });


      mBuilder.setNegativeButton(mContext.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
          }
      });



    }


  }

  public void show() {
    if (mBuilder != null) {
      AlertDialog d = mBuilder.create();
      d.show();
    }
  }



}
