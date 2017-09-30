package com.algonquincollege.bowe0145.hilo;

/**
 *  To implement, test and deploy a guess a number Android app
 *  @author Ryan Bowes (bowe0145@algonquinlive.com)
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {

    private static final String ABOUT_DIALOG_TAG = "About Dialog";

    Random rnd = new Random();
    int theNumber = 0;
    int guesses = 0;
    boolean hasWon = false;

    // Returns the random number and creates one (as well as resets guesses) if it doesn't exist
    public int getRandomNumber () {
        if (theNumber != 0) {
            return theNumber;
        } else {
            return getRandomNumber(true);
        }
    }
    // This creates a new random number and resets the guesses (Essentially all of the reset logic)
    public int getRandomNumber (boolean newNum) {
        // I should check the value of the argument but it's literally never going to be used
        int min = 1;
        int max = 1000;

        theNumber = rnd.nextInt(max) + min;
        guesses = 0;
        hasWon = false;

        return theNumber;
    }

    // Using the variable userGuess here. Because it's called for in the assignment
    public void winType (int userGuess) {
        //if the user takes 5 or less guesses, display a Toast message: "Superior win!";
        // if the user takes 6 to 10 guesses, display a Toast message: "Excellent win!";
        // if the user takes more than 10 guesses, display a Toast message: "Please Reset!"

        if (theNumber == 0) {
            getRandomNumber(true);
            winType(userGuess);
        }

        // The guess button will only display the "Please Reset" message if the user takes more than 10 guesses.
        // It will not recognize correct guesses after this point.
        if (guesses > 10 || hasWon) {
            Toast.makeText( getApplicationContext(),  "Please Reset!", Toast.LENGTH_SHORT).show();
            return;
        }

        int guessType = 0;
        guesses++;
        // Guess types
        if (userGuess == theNumber) {
            // They won; display a win message
            hasWon = true;
            if (guesses <= 5) {
                Toast.makeText( getApplicationContext(),  "Superior win!", Toast.LENGTH_SHORT ).show();
                return;
            } else if (guesses >= 6 && guesses <= 10) {
                Toast.makeText(getApplicationContext(), "Excellent win!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (userGuess < theNumber) {
            // Too low
            Toast.makeText(getApplicationContext(), "Too low!", Toast.LENGTH_SHORT).show();
        } else if (userGuess > theNumber) {
            // Too high
            Toast.makeText(getApplicationContext(), "Too high!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // THE NUMBER
        int theNumber = 0;
        Random rnd = new Random();

        // Create references to each UI element that we'll need
        final Button guessButton = (Button)findViewById(R.id.buttonGuess);
        final Button resetButton = (Button)findViewById(R.id.buttonReset);
        final EditText guessEditText = (EditText)findViewById(R.id.editTextGuess);

        // Guess Button Click Listener
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guessText = guessEditText.getText().toString();

                // User clicked guess, make sure that they have the correct data
                if (!guessText.isEmpty()) {
                    // There's data. Let's make sure that it's valid data now

                    // Check if everything is a digit
                    for (int i = 0; i < guessText.length(); i++) {
                        if (!Character.isDigit(guessText.charAt(i))) {
                            guessEditText.setError("Please use digits only");
                            guessEditText.requestFocus();
                            return;
                        }
                    }

                    // Now we know they are all digits so we don't have to worry about errors parsing

                    // Check the length
                    if (guessText.length() >= 4) {
                        if (Integer.parseInt(guessText) > 1000) {
                            // It won't be over 1000 if there aren't 4+ numbers
                            guessEditText.setError("Please use a number between 1 and 1000");
                            guessEditText.requestFocus();
                            return;
                        } else {
                            guessEditText.setError("Please use no more than 4 digits");
                            guessEditText.requestFocus();
                            return;
                        }
                    }

                    // Check if they just put a 0 in
                    if (guessText.length() == 1) {
                        try {
                            Integer.parseInt(guessText.substring(0, 1));

                            if (Integer.parseInt(guessText.substring(0, 1)) == 0) {
                                guessEditText.setError("Please use a number over 0 and under 1000");
                                guessEditText.requestFocus();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            guessEditText.setError("Please use digits only");
                            guessEditText.requestFocus();
                            return;
                        }
                    }

                    // When we get here we should only have valid input
                    winType(Integer.parseInt(guessText));
                    // End of the digit validation
                } else {
                    guessEditText.setError("You need to guess a number!");
                    guessEditText.requestFocus();
                }
            }
        });

        // Reset Button Click Listener
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a new number
                getRandomNumber(true);
            }
        });

        // Reset Button Long Click Listener
        resetButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText( getApplicationContext(),  Integer.toString(getRandomNumber()), Toast.LENGTH_LONG ).show();

                // Returning true so that it doesn't propagate
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            DialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
