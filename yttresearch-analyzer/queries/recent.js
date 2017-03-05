db.getCollection('processedVideos').aggregate([
    { "$addFields" : 
        { a : "$collected_at_timestamp", b : "$published_at_timestamp"}

     },
    { "$addFields" : 
        { diff :  { $subtract : [ "$a", "$b"]}}

     },
    { "$match" : 
        {$and :
            [  {  "diff" :  {$lte : 8640000000   }
                
            
               }
                ,
         
                {"artificial_category" : 1},
            ]
        }
    },
  
    { "$project" : { total_views: "$total_views", total_tweets : "$total_tweets" }},
    { "$sample" : {"size" :5 } },

]);

