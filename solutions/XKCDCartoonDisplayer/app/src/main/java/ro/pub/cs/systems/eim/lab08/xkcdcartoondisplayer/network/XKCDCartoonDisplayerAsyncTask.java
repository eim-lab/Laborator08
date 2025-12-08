package ro.pub.cs.systems.eim.lab08.xkcdcartoondisplayer.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.pub.cs.systems.eim.lab08.xkcdcartoondisplayer.entities.XKCDCartoonInformation;
import ro.pub.cs.systems.eim.lab08.xkcdcartoondisplayer.general.Constants;

public class XKCDCartoonDisplayerAsyncTask extends AsyncTask<String, Void, XKCDCartoonInformation> {

    // Use WeakReferences to avoid memory leaks
    private final WeakReference<TextView> xkcdCartoonTitleTextViewRef;
    private final WeakReference<ImageView> xkcdCartoonImageViewRef;
    private final WeakReference<TextView> xkcdCartoonUrlTextViewRef;
    private final WeakReference<Button> previousButtonRef;
    private final WeakReference<Button> nextButtonRef;

    private class XKCDCartoonButtonClickListener implements Button.OnClickListener {
        private final String xkcdComicUrl;

        public XKCDCartoonButtonClickListener(String xkcdComicUrl) {
            this.xkcdComicUrl = xkcdComicUrl;
        }

        @Override
        public void onClick(View view) {
            // Re-check references before executing a new task
            if (xkcdCartoonTitleTextViewRef.get() != null) {
                new XKCDCartoonDisplayerAsyncTask(
                        xkcdCartoonTitleTextViewRef.get(),
                        xkcdCartoonImageViewRef.get(),
                        xkcdCartoonUrlTextViewRef.get(),
                        previousButtonRef.get(),
                        nextButtonRef.get()
                ).execute(xkcdComicUrl);
            }
        }
    }

    public XKCDCartoonDisplayerAsyncTask(TextView xkcdCartoonTitleTextView, ImageView xkcdCartoonImageView, TextView xkcdCartoonUrlTextView, Button previousButton, Button nextButton) {
        this.xkcdCartoonTitleTextViewRef = new WeakReference<>(xkcdCartoonTitleTextView);
        this.xkcdCartoonImageViewRef = new WeakReference<>(xkcdCartoonImageView);
        this.xkcdCartoonUrlTextViewRef = new WeakReference<>(xkcdCartoonUrlTextView);
        this.previousButtonRef = new WeakReference<>(previousButton);
        this.nextButtonRef = new WeakReference<>(nextButton);
    }

    @Override
    public XKCDCartoonInformation doInBackground(String... urls) {
        if (urls == null || urls.length == 0) {
            return null;
        }
        String initialUrl = urls[0];
        XKCDCartoonInformation xkcdCartoonInformation = new XKCDCartoonInformation();
        OkHttpClient httpClient = new OkHttpClient();

        try {
            // 1. Get the HTML page source
            Request pageRequest = new Request.Builder().url(initialUrl).build();
            Response pageResponse = httpClient.newCall(pageRequest).execute();
            String pageSourceCode = null;
            if (pageResponse.isSuccessful() && pageResponse.body() != null) {
                pageSourceCode = pageResponse.body().string();
            }

            if (pageSourceCode != null) {
                Document document = Jsoup.parse(pageSourceCode);
                Element htmlTag = document.child(0);

                // 2. Parse cartoon title
                Element divTagIdCtitle = htmlTag.getElementsByAttributeValue(Constants.ID_ATTRIBUTE, Constants.CTITLE_VALUE).first();
                if (divTagIdCtitle != null) {
                    xkcdCartoonInformation.setCartoonTitle(divTagIdCtitle.ownText());
                }

                // 3. Parse cartoon image URL
                Element divTagIdComic = htmlTag.getElementsByAttributeValue(Constants.ID_ATTRIBUTE, Constants.COMIC_VALUE).first();
                if (divTagIdComic != null) {
                    String cartoonInternetAddress = divTagIdComic.getElementsByTag(Constants.IMG_TAG).attr(Constants.SRC_ATTRIBUTE);
                    String cartoonUrl = Constants.HTTPS_PROTOCOL + cartoonInternetAddress;
                    xkcdCartoonInformation.setCartoonUrl(cartoonUrl);

                    // 4. Download the cartoon image bitmap
                    Request imageRequest = new Request.Builder().url(cartoonUrl).build();
                    Response imageResponse = httpClient.newCall(imageRequest).execute();
                    if (imageResponse.isSuccessful() && imageResponse.body() != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(imageResponse.body().byteStream());
                        xkcdCartoonInformation.setCartoonBitmap(bitmap);
                    }
                }

                // 5. Parse previous and next cartoon URLs
                Element aTagRelPrev = htmlTag.getElementsByAttributeValue(Constants.REL_ATTRIBUTE, Constants.PREVIOUS_VALUE).first();
                if (aTagRelPrev != null) {
                    String previousCartoonInternetAddress = Constants.XKCD_INTERNET_ADDRESS + aTagRelPrev.attr(Constants.HREF_ATTRIBUTE);
                    xkcdCartoonInformation.setPreviousCartoonUrl(previousCartoonInternetAddress);
                }

                Element aTagRelNext = htmlTag.getElementsByAttributeValue(Constants.REL_ATTRIBUTE, Constants.NEXT_VALUE).first();
                if (aTagRelNext != null) {
                    String nextCartoonInternetAddress = Constants.XKCD_INTERNET_ADDRESS + aTagRelNext.attr(Constants.HREF_ATTRIBUTE);
                    xkcdCartoonInformation.setNextCartoonUrl(nextCartoonInternetAddress);
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, Objects.requireNonNull(ioException.getMessage()));
        }
        return xkcdCartoonInformation;
    }

    @Override
    protected void onPostExecute(final XKCDCartoonInformation xkcdCartoonInformation) {
        // Safely get references to UI elements
        TextView xkcdCartoonTitleTextView = xkcdCartoonTitleTextViewRef.get();
        ImageView xkcdCartoonImageView = xkcdCartoonImageViewRef.get();
        TextView xkcdCartoonUrlTextView = xkcdCartoonUrlTextViewRef.get();
        Button previousButton = previousButtonRef.get();
        Button nextButton = nextButtonRef.get();

        // Check if UI elements are still available before updating them
        if (xkcdCartoonInformation == null || xkcdCartoonTitleTextView == null) {
            return;
        }

        String cartoonTitle = xkcdCartoonInformation.getCartoonTitle();
        if (cartoonTitle != null) {
            xkcdCartoonTitleTextView.setText(cartoonTitle);
        }

        Bitmap cartoonBitmap = xkcdCartoonInformation.getCartoonBitmap();
        if (cartoonBitmap != null && xkcdCartoonImageView != null) {
            xkcdCartoonImageView.setImageBitmap(cartoonBitmap);
        }

        String cartoonUrl = xkcdCartoonInformation.getCartoonUrl();
        if (cartoonUrl != null && xkcdCartoonUrlTextView != null) {
            xkcdCartoonUrlTextView.setText(cartoonUrl);
        }

        String previousCartoonUrl = xkcdCartoonInformation.getPreviousCartoonUrl();
        if (previousCartoonUrl != null && previousButton != null) {
            previousButton.setOnClickListener(new XKCDCartoonButtonClickListener(previousCartoonUrl));
        }

        String nextCartoonUrl = xkcdCartoonInformation.getNextCartoonUrl();
        if (nextCartoonUrl != null && nextButton != null) {
            nextButton.setOnClickListener(new XKCDCartoonButtonClickListener(nextCartoonUrl));
        }
    }
}
