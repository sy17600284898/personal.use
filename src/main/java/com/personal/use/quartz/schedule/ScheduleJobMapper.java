package com.personal.use.quartz.schedule;



import com.personal.use.quartz.job.ScheduleJob;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * QuestionnaireMapper
 *
 * @author: zy
 * @date: 2019-07-28 14:33:10
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public interface ScheduleJobMapper {

     /**
      * getById
      * @param id
      * @return
      */
     ScheduleJob selectById(int id);

     /**
      * updateById
      * @param job
      */
     void updateById(ScheduleJob job);

     /**
      * removeById
      * @param id
      */
     void deleteById(int id);

     /**
      * save
      * @param job
      */
     void save(ScheduleJob job);

     /**
      * list
      * @return
      */
     List<ScheduleJob> list();

     /**
      * updateStatusBatch
      * @param status
      */
     void updateStatusBatch(@Param("status") int status);

     /**
      * selectByIdAndFlag
      *
      * @param id
      * @return
      */
     ScheduleJob selectByIdAndFlag(int id);
}


