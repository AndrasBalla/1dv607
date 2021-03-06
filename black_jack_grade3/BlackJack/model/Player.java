package BlackJack.model;

import BlackJack.controller.IVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class Player implements Subject {
    private ArrayList observers;
    private List<Card> m_hand;
    protected final int g_maxScore = 21;

    public Player() {
        m_hand = new LinkedList<>();
        observers = new ArrayList();
    }

    public void DealCard(Card a_addToHand) {
        m_hand.add(a_addToHand);
        notifyObservers();
    }

    public Iterable<Card> GetHand() {
        return m_hand;
    }

    public void ClearHand() {
        m_hand.clear();
    }

    public void ShowHand() {
        for (Card c : m_hand) {
            c.Show(true);
        }
    }

    public int CalcScore() {
        // the number of scores is dependant on the number of scorable values
        // as it seems there is no way to do this check at compile time in java ?!
        // cardScores[13] = {...};
        int cardScores[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        assert (cardScores.length == Card.Value.Count.ordinal()) : "Card Scores array size does not match number of card values";


        int score = 0;

        for (Card c : GetHand()) {
            if (c.GetValue() != Card.Value.Hidden) {
                score += cardScores[c.GetValue().ordinal()];
            }
        }

        if (score > g_maxScore) {
            for (Card c : GetHand()) {
                if (c.GetValue() == Card.Value.Ace && score > g_maxScore) {
                    score -= 10;
                }
            }
        }

        return score;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            Observer observer = (Observer)observers.get(i);
            observer.update();
        }
    }

    @Override
    public void accept(IVisitor visitor){}
}