db.getCollection('videos').aggregate([
    { "$unwind" : "$days"},
    { "$match" : 
        {$and :
            [
                {"days.day" :  {$gte : 0}},
                {"category_id": 24},
                
            ]
        }
    },
    { "$group" : 
        { _id : "$_id", sum : { $sum : "$days.view_count"}
    }},
    { "$sort" : { "sum"  : -1}},
    { "$limit" : 5 },
    { "$project" : {total_views:"$sum"}}
]);