/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

/**
 *  Decides what to do with a paired read. One of these behaviors is likely to be incredibly stupid.
 * 
 * Merge: Combines the two reads, selecting for the lowest error.
 * Discard: Keeps only the read with the lowest total error.
 * Flip: Keeps both, flipping the bases on the second read.
 * Dumb: Keeps them both with no processing.
 * @author Kris
 */
public enum PairBehavior {
    MERGE, DISCARD, FLIP, DUMB
}