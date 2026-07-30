package com.lms.sch.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.lms.sch.R;
import com.lms.sch.interfaces.SeeMoreTextViewListeners;
import com.lms.sch.interfaces.onTextClicked;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeeMoreTextView extends AppCompatTextView {

    public Integer textMaxLength = 100;
    public Integer seeMoreTextColor = R.color.colorPrimary;
    public int hashtagColor = ContextCompat.getColor(getContext(), R.color.colorAccent);

    private String collapsedTextWithSeeMoreButton;
    private String expandedTextWithSeeMoreButton;
    private String orignalContent;

    private SpannableString collapsedTextSpannable;
    private SpannableString expandedTextSpannable;

    private Boolean isExpanded = false;
    public SeeMoreTextViewListeners seeMoreTextViewListeners;
    private String seeMore = "show more", seeLess = "show less";

    private com.lms.sch.interfaces.onTextClicked onTextClicked;


    public void setOnTextClicked(onTextClicked onTextClicked) {
        this.onTextClicked = onTextClicked;
    }

    public SeeMoreTextView(Context context) {
        super(context);
    }

    public SeeMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeeMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //set max length of the string text
    public void setTextMaxLength(Integer maxLength) {
        textMaxLength = maxLength;
    }

    public void setSeeMoreTextColor(Integer color) {
        seeMoreTextColor = color;
    }

    public void expandText(Boolean expand) {
        if (expand) {
            isExpanded = true;
            setTextWithHashTag(expandedTextSpannable);
        } else {
            isExpanded = false;
            setTextWithHashTag(collapsedTextSpannable);
        }

    }

    public void setSeeMoreText(String seeMoreText, String seeLessText) {
        seeMore = seeMoreText;
        seeLess = seeLessText;
    }

    public Boolean isExpanded() {
        return isExpanded;
    }

    //toggle the state
    public void toggle() {
        if (isExpanded) {
            isExpanded = false;
            setTextWithHashTag(collapsedTextSpannable);
            if (seeMoreTextViewListeners != null) seeMoreTextViewListeners.onCollapsed();
        } else {
            isExpanded = true;
            setTextWithHashTag(expandedTextSpannable);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setContent(String text) {
        this.setVisibility(text.isEmpty() ? View.GONE : View.VISIBLE);
        if (text.isEmpty()) return;
        orignalContent = text;
        this.setMovementMethod(LinkMovementMethod.getInstance());
        //show see more
        if (orignalContent.length() >= textMaxLength) {
            collapsedTextWithSeeMoreButton = orignalContent.substring(0, textMaxLength) + "... " + seeMore;
            expandedTextWithSeeMoreButton = orignalContent + " " + seeLess;

            //creating spannable strings
            collapsedTextSpannable = new SpannableString(collapsedTextWithSeeMoreButton);
            expandedTextSpannable = new SpannableString(expandedTextWithSeeMoreButton);


            collapsedTextSpannable.setSpan(clickableSpan, textMaxLength + 4, collapsedTextWithSeeMoreButton.length(), 0);
            collapsedTextSpannable.setSpan(new StyleSpan(Typeface.NORMAL), textMaxLength + 4, collapsedTextWithSeeMoreButton.length(), 0);
            collapsedTextSpannable.setSpan(new RelativeSizeSpan(.9f), textMaxLength + 4, collapsedTextWithSeeMoreButton.length(), 0);

            expandedTextSpannable.setSpan(clickableSpan, orignalContent.length() + 1, expandedTextWithSeeMoreButton.length(), 0);
            expandedTextSpannable.setSpan(new StyleSpan(Typeface.NORMAL), orignalContent.length() + 1, expandedTextWithSeeMoreButton.length(), 0);
            expandedTextSpannable.setSpan(new RelativeSizeSpan(.9f), orignalContent.length() + 1, expandedTextWithSeeMoreButton.length(), 0);


            SpannableString hashtagintitle = isExpanded ? expandedTextSpannable : collapsedTextSpannable;
            setTextWithHashTag(hashtagintitle);

        } else {
            //to do: don't show see more
            SpannableString hashtagintitle = new SpannableString(orignalContent);
            setTextWithHashTag(hashtagintitle);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextClicked != null) {
                    if (getTag() == null || !getTag().equals("spanClicked")) {
                        onTextClicked.onTextClicked();
                    }
                    if (orignalContent.length() >= textMaxLength) {
                        toggle();
                    }
                }
                setTag("textClicked");

            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onTextClicked != null) {
                    onTextClicked.onTextLongClicked();
                }
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING  // Ignore device's setting. Otherwise, you can use FLAG_IGNORE_VIEW_SETTING to ignore view's setting.
                );
                showPopup();
                setTag("textLongClicked");
                return false;
            }
        });

        //selectable background
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setForeground(getContext().getDrawable(outValue.resourceId));

    }

    private void showPopup() {
       /* try {
            PopupMenu pop = new PopupMenu(getContext().getApplicationContext(), this);
            pop.getMenuInflater().inflate(R.menu.popup_copy_clipboard, pop.getMenu());
            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.copy_clipboard) {
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Uynite Text", orignalContent);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getContext(), R.string.text_copied_to_clipboard, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            pop.show();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("TAG", "SeeMoreTextView.java showPopup:Exception " + e);
            }
        }*/
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            // to prevent toggle when long click on "show more/less"
            if (getTag() == null || !getTag().equals("textLongClicked")) {
                toggle();
                setTag("spanClicked");
            } else {
                setTag("");

            }

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(getResources().getColor(seeMoreTextColor));
        }
    };

    public void setTextWithHashTag(SpannableString hashtagintitle) {
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashtagintitle);
        while (matcher.find()) {
            String ss = orignalContent.substring(matcher.start(), matcher.end());
            hashtagintitle.setSpan(new ForegroundColorSpan(hashtagColor), matcher.start(), matcher.end(), 0);
            hashtagintitle.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                   // HashTagPostsActivity.Companion.open(getContext(), ss);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, matcher.start(), matcher.end(), 0);
        }
       // Matcher matcher1 = Pattern.compile("http://").matcher(hashtagintitle);
        Matcher matcher2 = Pattern.compile("https://([A-Za-z0-9_/.?=-]+)").matcher(hashtagintitle);
        while (matcher2.find()) {
            String ss = orignalContent.substring(matcher2.start(), matcher2.end());
            hashtagintitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hyper_link)), matcher2.start(), matcher2.end(), 0);
            hashtagintitle.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent openURL = new Intent(Intent.ACTION_VIEW);
                    openURL.setData(Uri.parse(ss));
                    getContext().startActivity(openURL);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, matcher2.start(), matcher2.end(), 0);
        }
        setText(hashtagintitle);
    }
}

