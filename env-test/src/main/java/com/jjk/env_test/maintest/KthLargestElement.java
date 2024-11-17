package com.jjk.env_test.maintest;
import java.util.Random;

public class KthLargestElement {

    // Method to find the kth largest element
    public static int findKthLargest(int[] nums, int k) {
        // Convert k to the corresponding index in a zero-based array
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    // QuickSelect algorithm implementation
    private static int quickSelect(int[] nums, int left, int right, int k) {
        if (left == right) { // If the list contains only one element
            return nums[left];
        }

        Random random = new Random();
        // Randomly select a pivot index
        int pivotIndex = left + random.nextInt(right - left + 1);
        pivotIndex = partition(nums, left, right, pivotIndex);

        // Check the pivot position against k
        if (k == pivotIndex) {
            return nums[k]; // The kth largest element found
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k); // Search left
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k); // Search right
        }
    }

    // Partition method
    private static int partition(int[] nums, int left, int right, int pivotIndex) {
        int pivotValue = nums[pivotIndex];
        
        System.out.println("pivotIndex " + pivotIndex + " " + nums.length);

        // Move pivot to the end
        swap(nums, pivotIndex, right);
        System.out.println("Array after partitioning: " + arrayToString(nums));

        int storeIndex = left;

        for (int i = left; i < right; i++) {
            // Move larger elements to the left
            if (nums[i] <= pivotValue) { // For kth largest, we want larger elements on the left
                swap(nums, storeIndex, i);
                System.out.println("Array after partitioning: " + arrayToString(nums));

                storeIndex++;
            }
        }

        // Move pivot to its final place
        swap(nums, storeIndex, right);
        
        // Print the array after partitioning
        System.out.println("Array after partitioning: " + arrayToString(nums));
        
        return storeIndex; // Return the pivot's final index
    }

    // Swap method for elements in the array
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // Utility method to convert array to string for printing
    private static String arrayToString(int[] nums) {
        StringBuilder sb = new StringBuilder();
        for (int num : nums) {
            sb.append(num).append(" ");
        }
        return sb.toString().trim();
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        int[] nums1 = {3, 2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 , 2 ,2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 , 2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 , 2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 , 2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 , 2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 , 2, 2, 2, 2, 2,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4  ,2}; // Input array
        int k1 = 2; // We want the 2nd largest element
        int result1 = findKthLargest(nums1, k1);
        System.out.println("The " + k1 + "th largest element is: " + result1); // Expected output: 5

     }
}


