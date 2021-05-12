package com.example.redditClone.models;

import java.util.*;
import java.util.Collections;

class MainJob
{
//     schedule jobs to maximize profit
    public static int scheduleJobs(List<Job> jobs, int T)
    {
        // stores maximum profit that can be earned by scheduling jobs
        int profit = 0;

        // array to store used and unused slots info
        int[] slot = new int[T];
        //fill all the array slots with -1's
        Arrays.fill(slot, -1);

        // consider each job in decreasing order of their profits
        for (Job job: jobs)
        {
            // search for next free slot and map the task to that slot
            for (int j = job.deadline - 1; j >= 0; j--)
            {
                // time slot is available i.e take the job
                // and take the profit
                if (slot[j] == -1)
                {
                    slot[j] = job.taskID;
                    profit += job.profit;
                    break;
                }
            }
        }


        int [] scheduledJobs = Arrays.stream(slot).filter(val-> val != -1).toArray();

        // print the scheduled jobs
        System.out.println("Here " + Arrays.toString(scheduledJobs));

        // return total profit that can be earned
        return profit;
    }

    public static void main(String[] args)
    {
        // List of given jobs. Each job has an identifier, a deadline and
        // profit associated with it
        List<Job> jobs = Arrays.asList(
                new Job(1, 9, 15), new Job(2, 2, 2),
                new Job(3, 5, 18), new Job(4, 7, 1),
                new Job(5, 4, 25), new Job(6, 2, 20),
                new Job(7, 5, 8), new Job(8, 7, 10),
                new Job(9, 4, 12), new Job(10, 3, 5)
        );

        // get the max deadline
         int max = jobs.stream().max((a,b) -> b.deadline - a.deadline).get().deadline;

        // Job maxDeadlineJob = jobs.stream().max((a,b) -> b.deadline - a.deadline).get();

        // stores maximum deadline that can be associated with a job

        final int T = max;
        // arrange the jobs in decreasing order of their profits
        Collections.sort(jobs, (a, b) -> b.profit - a.profit);

        // schedule jobs and calculate maximum profit
        System.out.println("Total Profit is: " + scheduleJobs(jobs, T));
    }
}
