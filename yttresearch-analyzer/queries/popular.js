db.getCollection('processedVideos').aggregate([
    { "$unwind" : "$days"},
    { "$match" : 
        {$and :
            [
                {"days.day" :  {$gte : 0}},
                {"artificial_category" : 1},
            ]
        }
    },
    { "$group" : 
        { _id : "$_id", sum : { $sum : "$days.views_added"}
    }},
    { "$sort" : { "sum"  : -1}},
    { "$limit" : 5 },
    { "$project" : {total_views:"$sum"}}
]);

