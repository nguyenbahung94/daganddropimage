package hung.testdraganddrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentMain extends Fragment {
    private ImageView imgattach;
    private String pictureImagePath = "";
    private static int SELECTED = 0;
    private LinearLayout attachments, layoutbefore, layoutafter;
    private ArrayList<String> listImage;
    private static final String IMAGEVIEW_TAG = "icon bitmap";
    private TextView tvbefore, tvafter;
    private View viewbefore, viewafter;
    private HorizontalScrollView scrollView, scrollviewadd;
    private boolean isopenbefore = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        imgattach = (ImageView) view.findViewById(R.id.attach);
        attachments = (LinearLayout) view.findViewById(R.id.attachmentss);
        layoutbefore = (LinearLayout) view.findViewById(R.id.before);
        layoutafter = (LinearLayout) view.findViewById(R.id.after);
        tvafter = (TextView) view.findViewById(R.id.tv_after);
        tvbefore = (TextView) view.findViewById(R.id.tv_before);
        viewafter = (View) view.findViewById(R.id.after_indicator);
        viewbefore = (View) view.findViewById(R.id.before_indicator);
        scrollView = (HorizontalScrollView) view.findViewById(R.id.scroll);
        tvafter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isopenbefore = false;
                layoutafter.setVisibility(View.VISIBLE);
                layoutbefore.setVisibility(View.GONE);
                viewafter.setVisibility(View.GONE);
                viewbefore.setVisibility(View.VISIBLE);
            }
        });
        tvbefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isopenbefore = true;
                layoutafter.setVisibility(View.GONE);
                layoutbefore.setVisibility(View.VISIBLE);
                viewafter.setVisibility(View.VISIBLE);
                viewbefore.setVisibility(View.GONE);
            }
        });
        viewbefore.setVisibility(View.GONE);
        layoutbefore.setVisibility(View.VISIBLE);
        listImage = new ArrayList<>();
        scrollView.setOnDragListener(new MyDragListener());
        attachments.setOnDragListener(new MyDragListener());
        imgattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera();
            }
        });
        return view;
    }

    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            galleryAddPic();
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                listImage.add(imgFile.getAbsolutePath());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.activity_show_a_picture, attachments, false);
                ImageView imagetam = (ImageView) myLayout.findViewById(R.id.image_show_a_picture);
                imagetam.setImageBitmap(myBitmap);
                imagetam.setTag(IMAGEVIEW_TAG);
                imagetam.setOnLongClickListener(new MyClickListener());
                attachments.addView(myLayout);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private final class MyClickListener implements View.OnLongClickListener {
        // called when the item is long-clicked
        @Override
        public boolean onLongClick(View view) {

            // TODO Auto-generated method stub
//
//            // create it from the object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, //data to be dragged
                    shadowBuilder, //drag shadow
                    view, //local data about the drag and drop operation
                    0   //no needed flags
            );
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private class MyDragListener implements View.OnDragListener {
        // Drawable normalShape = getResources().getDrawable(R.drawable.read);
        Drawable targetShape = getResources().getDrawable(R.drawable.white);
        Drawable normalshape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            // Handles each of the expected events
            switch (event.getAction()) {
                //signal for the start of a drag and drop operation.
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;

                //the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:


                    v.setBackground(targetShape);   //change the shape of the view
                    break;
                //the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalshape);   //change the shape of the view back to normal
                    break;
                //drag shadow has been released,the drag point is within the bounding box of the View
                case DragEvent.ACTION_DROP:
                    // if the view is the bottomlinear, we accept the drag item
                    if (v == getActivity().findViewById(R.id.attachmentss)) {
                        View view = (View) event.getLocalState();
//                        ViewGroup viewGroup = (ViewGroup) view.getParent();
//                        viewGroup.removeView(view);
//                       LinearLayout linearLayout=(LinearLayout)v;
//                       linearLayout.addView(view);
                        if (isopenbefore) {
                            layoutbefore.removeView(view);
                        }
                        if (!isopenbefore) {
                            layoutafter.removeView(view);
                        }
                        attachments.addView(view);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        if (isopenbefore) {
                            View view = (View) event.getLocalState();
                            attachments.removeView(view);
//                            ViewGroup viewGroup = (ViewGroup) view.getParent();
//                            viewGroup.removeView(view);
//                       LinearLayout linearLayout=(LinearLayout)v;
//                       linearLayout.addView(view);
                            layoutbefore.addView(view);
                            view.setVisibility(View.VISIBLE);
                        } else {
                            if (!isopenbefore) {
                                View view = (View) event.getLocalState();
//                                ViewGroup viewGroup = (ViewGroup) view.getParent();
//                                viewGroup.removeView(view);
//                       LinearLayout linearLayout=(LinearLayout)v;
//                       linearLayout.addView(view);
                                attachments.removeView(view);
                                layoutafter.addView(view);
                                view.setVisibility(View.VISIBLE);
                            }
                        }

                    }


                    //  if (v == findViewById(R.id.layout_addtask)) {
//                        View view = (View) event.getLocalState();
//                        ViewGroup viewGroup = (ViewGroup) view.getParent();
//                        viewGroup.removeView(view);
//                        LinearLayout contaiView = (LinearLayout) v;
//                        contaiView.addView(view);
//                        view.setVisibility(View.VISIBLE);
                    //    }
                    break;
                //the drag and drop operation has concluded.
                case DragEvent.ACTION_DRAG_ENDED:
                    //    v.setBackground(normalshape);   //go back to normal shape
                default:
                    break;
            }
            return true;
        }
    }

}

