bulkAppControllers.controller("feedCtrl", function($scope, $rootScope, $http, $timeout) {
	$scope.curTitle = "Feed";
	$scope.curUrl = "event";
	$scope.curEvents = [];
	$scope.feedData = {};
	console.log("feed");
/*
	$.post("/event-feed", {id: $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.$apply(function() {
				$scope.curEvents = responseObject.events;
			});
			console.log(responseObject);
	});
*/

	function getLoc(callback) {
            if (navigator.geolocation) {
              var startPos;
              var geoOptions = {
                 maximumAge:  60 * 1000,
                 timeout: 10 * 1000
              }

              var geoSuccess = function(position) {
                startPos = position;
                $scope.lat = startPos.coords.latitude;
                $scope.lng = startPos.coords.longitude;

                console.log("retrieved location");
                console.log($scope.lat);
                console.log($scope.lng);
                callback();
              };
              var geoError = function(error) {
                console.log('Error occurred. Error code: ' + error.code);
                // error.code can be:
                //   0: unknown error
                //   1: permission denied
                //   2: position unavailable (error response from location provider)
                //   3: timed out
                $scope.lat = null;
                $scope.lng = null;
                callback();
              };

              navigator.geolocation.getCurrentPosition(geoSuccess, geoError, geoOptions);

            }
            else {
                console.log('Geolocation is not supported for this Browser/OS version yet.');
                callback();
            }
           }


    function getFeed() {
                console.log("STUFF");
                console.log($scope.lat);
                console.log($scope.lng);
                $scope.feedData["id"] = $rootScope.account.id;
                $scope.feedData["lat"] = $scope.lat;
                $scope.feedData["lng"] = $scope.lng;
                console.log($scope.feedData);
    		$.post("/event-feed", $scope.feedData, function(responseJSON) {
            			responseObject = JSON.parse(responseJSON);
            			$timeout(function() {$scope.curEvents = responseObject.events;}, 0);
            			console.log(responseObject);
            	})
            }
    getLoc(getFeed);
		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/
});