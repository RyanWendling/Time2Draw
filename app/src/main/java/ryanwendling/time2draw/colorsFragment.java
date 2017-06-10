package ryanwendling.time2draw;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by wendlir on 5/25/17.
 */
public class colorsFragment extends Fragment {

    Button toggle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.colors_fragment_layout,
                container, false);

        toggle = (Button)view.findViewById(R.id.toggleButton);
        toggle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(getActivity());
                        a_builder.setMessage("Have fun drawing with a variety of colors and brush sizes! There are also options to undo and redo your last brush stroke along with a database feature to save your works of art! Simply name your artwork and click the SAVE ARTWORK button. You can use this same naming feature to pull up previously saved masterpieces. If your artwork seems lackluster, feel free to CLEAR ALL to start from scratch."
                        );
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Here's how Time2Draw works: ");
                        alert.show();
                    }
                }
        );
        return view;
    }
}
