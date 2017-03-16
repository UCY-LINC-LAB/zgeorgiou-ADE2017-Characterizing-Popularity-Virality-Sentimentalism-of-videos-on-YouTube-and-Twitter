db.getCollection('videos').updateMany({"meta.current_date":14},{$set : {"meta.processed" : false, "meta.finished":true}})
