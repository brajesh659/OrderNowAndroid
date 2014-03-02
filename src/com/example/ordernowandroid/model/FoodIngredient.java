package com.example.ordernowandroid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.data.menu.Ingredient;
import com.data.menu.IngredientOption;

public class FoodIngredient implements Serializable {

    private static final long serialVersionUID = 1L;
    private Ingredient ingredient;
    private ArrayList<IngredientOptionView> options;
    private ArrayList<IngredientOptionView> selectedOptions;

    public ArrayList<IngredientOptionView> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<IngredientOptionView> selectedOptions) {
        if (selectedOptions != null) {
            prepareSelectedOptions(selectedOptions);
        }
    }

    public void setPreSelectedOptions(List<IngredientOption> selectedOptions) {
        if (selectedOptions != null) {
            List<IngredientOption> options = this.ingredient.getIngredientOptions();
            for (IngredientOption option : options) {
                if (selectedOptions.contains(option)) {
                    IngredientOptionView opv = new IngredientOptionView(option);
                    opv.setSelected(true);
                    this.selectedOptions.add(opv);
                }
            }
        }
    }

    public List<IngredientOptionView> getIngredientOptions() {
        return options;
    }

    public void setIngredientOptionsView(List<IngredientOption> selectedOptions) {
        setIngredientOptionsView();
        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            for (IngredientOptionView option : options) {
                IngredientOption ingOption = option.getOption();
                if (selectedOptions.contains(ingOption)) {
                    option.setSelected(true);
                    this.selectedOptions.add(option);
                }
            }
        }
    }

    public void setIngredientOptionsView() {
        if (this.ingredient != null) {
            List<IngredientOption> ingoptions = this.ingredient.getIngredientOptions();
            if (ingoptions != null) {
                for (IngredientOption option : ingoptions) {
                    IngredientOptionView opv = new IngredientOptionView(option);
                    this.options.add(opv);
                }
            }
        }
    }

    public FoodIngredient(Ingredient ingredient, ArrayList<IngredientOption> selectedOptions) {
        super();
        this.ingredient = ingredient;
        this.selectedOptions = new ArrayList<IngredientOptionView>();
        this.options = new ArrayList<IngredientOptionView>();
        setIngredientOptionsView(selectedOptions);
    }

    public FoodIngredient(Ingredient ingredient) {
        super();
        this.ingredient = ingredient;
        this.selectedOptions = new ArrayList<IngredientOptionView>();
        this.options = new ArrayList<IngredientOptionView>();
        setIngredientOptionsView();
    }

    public String getTitle() {
        return ingredient.getTitle();
    }

    public List<IngredientOption> getOptions() {
        return ingredient.getIngredientOptions();
    }

    public void addOption(IngredientOptionView optionview) {
        if (!selectedOptions.contains(optionview)) {
            selectedOptions.add(optionview);
        }
    }

    public void removeOption(IngredientOptionView optionview) {
        if (selectedOptions.contains(optionview)) {
            selectedOptions.remove(optionview);
        }
    }

    public String getBitMapText() {
        String text = getTitle();
        if (getMinOptionSelection() > 0) {
            text += "\nMin : " + getMinOptionSelection();
        }

        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            text += "\n\nSelected:\n";
            for (IngredientOptionView option : selectedOptions) {
                text += option.getOptionName() + "\n";
            }
        } else {
            text += "\n\nSelected: NONE";
        }
        return text;
    }

    public void prepareSelectedOptions(List<IngredientOptionView> selectedOptions) {
        this.selectedOptions = new ArrayList<IngredientOptionView>();
        if (selectedOptions != null) {
            List<IngredientOption> options = ingredient.getIngredientOptions();
            for (IngredientOption option : options) {
                IngredientOptionView opv = new IngredientOptionView(option);
                if (selectedOptions.contains(opv)) {
                    this.selectedOptions.add(opv);
                }
            }
        }
    }

    public int getMinOptionSelection() {
        return ingredient.getMinOptionSelection();
    }

    public boolean isMinOptionsSelected() {
        if (getMinOptionSelection() == 0) {
            return true;
        }
        if (selectedOptions != null && selectedOptions.size() >= getMinOptionSelection()) {
            return true;
        }
        return false;
    }

}
