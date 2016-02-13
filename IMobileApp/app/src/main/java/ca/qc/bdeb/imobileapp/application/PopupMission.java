package ca.qc.bdeb.imobileapp.application;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import ca.qc.bdeb.imobileapp.R;


/**
 * Le popup des missions.
 *
 * @author Alexandre Sasseville et Vincent Bissonnette
 */
public class PopupMission {

    private Activity activity;

    private View popupView;
    private ImageView imgIcon;
    private ImageView imgReussite;
    private TextView txtTitre;
    private TextView txtDifficulte;
    private TextView txtDescription;
    private Button btnOk;
    private Button btnCancel;
    private EditText edtReponse;

    private PopupWindow popupWindow;

    //private Mission mission = null;

    private PopupResult pmr;

    /**
     * Constructeur du popup
     *
     * @param activity L'activity ou le popup sera affiché
     * @param pmr Une classe qui veut recevoir le résultat
     */
    public PopupMission(Activity activity, PopupResult pmr) {
        this.activity = activity;
        this.pmr = pmr;

        LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.layout_popup_mission, null);
        popupWindow = new PopupWindow(popupView,
                AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.MATCH_PARENT,
                true);

        this.recupererComposantLayout();
        this.initialiserBouton();
    }

    /**
     * Récupération des composantes
     */
    private void recupererComposantLayout() {

        txtDescription = (TextView) popupView.findViewById(R.id.popup_mission_txt_description);
        btnOk = (Button) popupView.findViewById(R.id.popup_mission_btn_add);
        btnCancel = (Button) popupView.findViewById(R.id.popup_mission_btn_cancel);
        edtReponse = (EditText)popupView.findViewById(R.id.popup_mission_edt_reponse);
    }

    /**
     * Initialiser les boutons
     */
    private void initialiserBouton() {
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pmr.onPopupResult(PopupResultChoices.SUCESS, edtReponse.getText().toString());

                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                pmr.onPopupResult(PopupResultChoices.FAIL, edtReponse.getText().toString());
                popupWindow.dismiss();
            }
        });
    }

    /**
     * Mettre les informaions de la mission sur le popup
     *
     * @param mission La mission
     */
    /*
    public void setMission(Mission mission) {
        this.mission = mission;

        TypeExercice typeExercice = mission.getTypeExercice();
        if (typeExercice == TypeExercice.COURSE) {
            imgIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.courir));
        } else if (typeExercice == TypeExercice.VELO) {
            imgIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.velo));
        }

        if (mission.getReussit()) {
            imgReussite.setVisibility(View.VISIBLE);
            btnOk.setEnabled(false);
        } else {
            imgReussite.setVisibility(View.GONE);
            btnOk.setEnabled(true);
        }

        txtTitre.setText(mission.getNom());
        txtDifficulte.setText(mission.getTypeDifficulte().getNom());
        txtDescription.setText(mission.getDescription());
    }
    */

    /**
     * Afficher le popup
     */
    public void show() {
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        //popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
}

