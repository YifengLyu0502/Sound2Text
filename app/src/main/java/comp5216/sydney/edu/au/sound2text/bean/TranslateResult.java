package comp5216.sydney.edu.au.sound2text.bean;

import java.util.ArrayList;
import java.util.List;

public class TranslateResult {
    public List<String> translation;

    public List<String> getTranslation() {
        return translation == null ? new ArrayList<String>() : translation;
    }
}
