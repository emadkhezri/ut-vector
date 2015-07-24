/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.emad;

import java.util.Arrays;

/**
 *
 * @author tnp
 */
public class test
{
    public static void main(String[] args)
    {
        int array[][] = new int[3][4];
        
        for(int i=0;i<3;i++)
            for(int j=0;j<4;j++)
            {
                array[i][j]=i;
            }
        System.out.println(array);
    }
}
