db.getCollection('videos').updateMany({"meta.current_date":3},{$set : {"meta.processed" : false, "meta.finished":true}})
