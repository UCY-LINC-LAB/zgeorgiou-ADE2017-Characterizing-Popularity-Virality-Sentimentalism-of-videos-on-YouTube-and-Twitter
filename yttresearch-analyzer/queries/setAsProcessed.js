db.getCollection('videos').updateMany({"meta.current_date":10},{$set : {"meta.processed" : false, "meta.finished":true}})
