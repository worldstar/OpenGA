package openga.util.sort;

/**
 * <p>Title: Quick sort algorithm with index seqeunce.</p>
 * <p>Description: The original code supports the sorting of values but it doesn't include
      the sequence of corresponding value. The code satisfies the requirement.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
/* Revised by Peter
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * This program demonstrates the use of the quick sort algorithm.  For
 * more information about this and other sorting algorithms, see
 * http://linux.wku.edu/~lamonml/kb.html
 *
 */

public class quickSort {
  public quickSort() {
  }

  void quickSort(int numbers[], int array_size, int indexes[]){
    q_sort(numbers, 0, array_size - 1, indexes);
  }

  void quickSort(double numbers[], int array_size, int indexes[]){
    q_sort(numbers, 0, array_size - 1, indexes);
  }
      
  void q_sort(int numbers[], int left, int right, int indexes[])
  {
    int pivot, l_hold, r_hold;
    int pivot_num;

    l_hold = left;
    r_hold = right;
    pivot = numbers[left];
    pivot_num = indexes[left];

    while (left < right)
    {
      while ((numbers[right] >= pivot) && (left < right))
        right--;
      if (left != right)
      {
        numbers[left] = numbers[right];
        indexes[left] = indexes[right];
        left++;
      }

      while ((numbers[left] <= pivot) && (left < right))
        left++;
      if (left != right)
      {
        numbers[right] = numbers[left];
        indexes[right] = indexes[left];
        right--;
      }
    }
    numbers[left] = pivot;
    indexes[left] = pivot_num;
    pivot = left;
    left = l_hold;
    right = r_hold;
    if (left < pivot)
      q_sort(numbers, left, pivot-1, indexes);
    if (right > pivot)
      q_sort(numbers, pivot+1, right, indexes);
  }

  /***********   For double data sorting           ******************/
  void quickSort(double numbers[], int array_size, int indexes[])
  {
    q_sort(numbers, 0, array_size - 1, indexes);
  }


  void q_sort(double numbers[], int left, int right, int indexes[])
  {
    double pivot;
    int l_hold, r_hold;
    int pivot_num;

    l_hold = left;
    r_hold = right;
    pivot = numbers[left];
    pivot_num = indexes[left];

    while (left < right)
    {
      while ((numbers[right] >= pivot) && (left < right))
        right--;
      if (left != right)
      {
        numbers[left] = numbers[right];
        indexes[left] = indexes[right];
        left++;
      }

      while ((numbers[left] <= pivot) && (left < right))
        left++;
      if (left != right)
      {
        numbers[right] = numbers[left];
        indexes[right] = indexes[left];
        right--;
      }
    }
    numbers[left] = pivot;
    indexes[left] = pivot_num;
    pivot = left;
    left = l_hold;
    right = r_hold;
    if (left < pivot)
      q_sort(numbers, left, (int)pivot-1, indexes);
    if (right > pivot)
      q_sort(numbers, (int)pivot+1, right, indexes);
  }

}
