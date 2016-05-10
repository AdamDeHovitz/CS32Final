bulkAppControllers
    .controller(
        "feedCtrl",
        function($scope, $rootScope, $http, $timeout) {
	        $scope.curTitle = "Feed";
	        $scope.curUrl = "event";
	        // $scope.curEvents = [];
	        $scope.feedData = {};
	        console.log("feed");

	        $scope.$on('$ionicView.enter', function() {
		        // console.log('UserMessages ionicView.enter');
		      $scope.loading = true;

	        });

	        $scope.$on('$ionicView.leave', function() {
		        $scope.loading = false;
	        });
	        /*
					 * $.post("/event-feed", {id: $rootScope.account.id},
					 * function(responseJSON) { responseObject = JSON.parse(responseJSON);
					 * $scope.$apply(function() { $scope.curEvents =
					 * responseObject.events; }); console.log(responseObject); });
					 */

	        function getLoc(callback) {
		        if (navigator.geolocation) {
			        var startPos;
			        var geoOptions = {
			          maximumAge : 2 * 1000,
			          timeout : 10 * 1000
			        }

			        var geoSuccess = function(position) {
				        startPos = position;
				        $rootScope.lat = startPos.coords.latitude;
				        $rootScope.lng = startPos.coords.longitude;
				        console.log("retrieved location");
				        console.log($rootScope.lat);
				        console.log($rootScope.lng);
				        $scope.loading = false;
				        callback();
			        };
			        var geoError = function(error) {
				        console.log('Error occurred. Error code: ' + error.code);
				        // error.code can be:
				        // 0: unknown error
				        // 1: permission denied
				        // 2: position unavailable (error response from location
								// provider)
				        // 3: timed out
				        $rootScope.lat = null;
				        $rootScope.lng = null;
				        $scope.loading = false;
				        callback();
			        };

			        navigator.geolocation.getCurrentPosition(geoSuccess, geoError,
			            geoOptions);

		        } else {
			        console
			            .log('Geolocation is not supported for this Browser/OS version yet.');
			            $scope.loading = false;
			        callback();
		        }
	        }


	        function getFeed() {
                            console.log("STUFF");
                            $scope.feedData["id"] = $rootScope.account.id;
                            $scope.feedData["lat"] = $rootScope.lat;
                            $scope.feedData["lng"] = $rootScope.lng;
                            console.log($rootScope.lat);
                            console.log($rootScope.lng);
                            console.log($scope.feedData);
                		$.post("/event-feed", $scope.feedData, function(responseJSON) {
                        			responseObject = JSON.parse(responseJSON);
                        			$timeout(function() {$scope.curEvents = responseObject.events;}, 0);
                        			console.log(responseObject);
                        	})
                        }
                if ($rootScope.lat != null) {
                    getFeed();
                }
                getLoc(getFeed);
                var rad = function(x) {
                    return x * Math.PI / 180;
                   };
	        $rootScope.getDistance = function(event) {
                    var R = 6378137; // Earth’s mean radius in meter
                    var dLat = rad(event.lat - $rootScope.lat);
                    var dLong = rad(event.lng - $rootScope.lng);
                    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(rad($rootScope.lat)) * Math.cos(rad(event.lat)) *
                    Math.sin(dLong / 2) * Math.sin(dLong / 2);
                    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    var d = R * c;
                    return d; // returns the distance in meter
                    };

	        /*
					 * if (!$rootScope.userId) { $location.path("/"); } var postData =
					 * $.param({ json: JSON.stringify({ userId: userId, }) });
					 * $http.post("/feedEntries", postData).success(function(data, status) {
					 * $scope.events = data.entries; });
					 */
        });