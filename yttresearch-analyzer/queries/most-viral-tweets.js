db.getCollection('tweets').aggregate([
    { "$group" : 
        { _id : "$video_id", count : { "$sum" : 1}
    }},
    { "$sort" : { "count"  : -1}},
    { "$limit" : 5 },
    { "$project" : {total_tweets:"$count"}}
]);