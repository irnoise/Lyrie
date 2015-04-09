package jp.ne.noise.lyrie.search.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import jp.ne.noise.lyrie.R;
import jp.ne.noise.lyrie.UIActionListener;


/**
 * Created by mgt on 2015/04/06.
 */
public class SearchWindow extends LinearLayout {
    public static final int WINDOW_ACTION_MODE_SWITCH = 0,
                                WINDOW_ACTION_MODE_SHOW   = 1,
                                WINDOW_ACTION_MODE_HIDE   = 2;
    private static final int SEARCH_MODE_TEXT           = 0,
                                SEARCH_MODE_PLAYING        = 1;
    private UIActionListener uiActionListener;
    private TextView header;
    private ViewFlipper viewFlipper;
    private boolean shown;
    private int searchMode;
    private String[] args;

    public SearchWindow(Activity activity, UIActionListener uiActionListener) {
        super(activity);

        this.uiActionListener = uiActionListener;

        LayoutInflater.from(activity).inflate(R.layout.search_window, this);
        // 何故かxml側のLayoutParamが読まれていないため、コード内で再定義
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        header = (TextView)findViewById(R.id.search_window_header);
        viewFlipper = (ViewFlipper)findViewById(R.id.search_window_viewflipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));

        setListener();
        shown = true;
        searchMode = SEARCH_MODE_TEXT;
        args = new String[3];
    }

    private void setListener() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == findViewById(R.id.search_window_search_button)) {
                    int i = 0;
                    for (String arg : args) {
                        EditText editText = (EditText)findViewById(R.id.search_windows_edittext_artist + i);
                        args[i] = editText.getText().toString();
                        i++;
                    }
                    uiActionListener.callback(SearchWindow.this);
                }
                if (v == findViewById(R.id.search_window_button_for_text)) {
                    if (searchMode == SEARCH_MODE_PLAYING) {
                        header.setText("文字を入力して検索");
                        viewFlipper.showPrevious();
                        searchMode = SEARCH_MODE_TEXT;
                    }
                }
                if (v == findViewById(R.id.search_window_button_for_playing)) {
                    if (searchMode == SEARCH_MODE_TEXT) {
                        header.setText("再生中の曲で検索");
                        viewFlipper.showNext();
                        searchMode = SEARCH_MODE_PLAYING;
                    }
                }
            }

        };

        findViewById(R.id.search_window_search_button).setOnClickListener(onClickListener);
        findViewById(R.id.search_window_button_for_text).setOnClickListener(onClickListener);
        findViewById(R.id.search_window_button_for_playing).setOnClickListener(onClickListener);
    }

    /**
     * 検索ウインドウの表示/非表示を切り替える
     */
    public void switchWindow(final int ACTION_MODE) {
        switch (ACTION_MODE) {
            case WINDOW_ACTION_MODE_SWITCH:
                if (shown) {
                    startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.out_to_up));
                    shown = false;
                } else {
                    startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_from_up));
                    shown = true;
                }
                break;
            case WINDOW_ACTION_MODE_SHOW:
                if (! shown) {
                    startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_from_up));
                    shown = true;
                }
                break;
            case WINDOW_ACTION_MODE_HIDE:
                if (shown) {
                    startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.out_to_up));
                    shown = false;
                }
                break;
            default:
                break;
        }

    }

    public boolean isShown() {
        return shown;
    }

    public String[] getArgs() {
        return args;
    }
}
