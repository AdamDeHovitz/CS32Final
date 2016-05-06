bulkAppControllers.controller("requestsCtrl", 
	function($scope, $rootScope, $http, $stateParams, $state, $timeout, $interval) {

	var eventId = $stateParams.eventId;
	$scope.$on('$ionicView.enter', function() {
		$scope.currentMsg = undefined;
	});

	$scope.$on('$ionicView.leave', function() {
		$scope.currentMsg = undefined;
	});

	var getRequests = function() {
		$.post("/user-requests", {"id": eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.requests = responseObject.requests;
        });
	}

	$timeout(getRequests, 0);
	//$scope.gettingReqs = $interval(getRequests, 5000);
	/*
	if ($state.current.name == "tab.events-requests") {
		$scope.curProfileUrl = "events-profile";
	} else {
		$scope.curProfileUrl = "profile";
	}*/
	
	$scope.goProfile = function(id) {
		if ($state.current.name == "tab.events-requests") {
	 		$state.go('tab.events-profile',{userId: id});
	 	} else {
		 	$state.go('tab.profile',{userId: id});
		}
	};

	$scope.deleteRequest = function(id, name) {
		var newList = [];
		for (var i =0 ; i < $scope.requests.length; i++) {
			console.log($scope.requests[i]);
			if ($scope.requests[i].id != id) {
				newList.push($scope.requests[i]);
			}
		}
		console.log(newList);
		$scope.requests = newList;
		$scope.currentMsg = name + "'s request has been removed.";

	}

	$scope.acceptRequest = function(id, name) {
		var newList = [];
		for (var i =0 ; i < $scope.requests.length; i++) {
			if ($scope.requests[i].id != id) {
				newList.push($scope.requests[i]);
			}
		}
		$.post("/event-join", postParams, 
			function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
		});
		$scope.requests = newList;
		$scope.currentMsg = name + " has been accepted.";
	}

	/*
	$.post("/event-view", {id: eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.event = responseObject;
			var isOwner = responseObject.authorId == $rootScope.account.id;

			$scope.isOwner = isOwner;

			console.log(responseObject);
	});

	$scope.requests = [{name: "Bob", img: "https://s-media-cache-ak0.pinimg.com/736x/30/6d/aa/306daa2d2a312f8ea44e9e49afc4b9d4.jpg", id: "1", rating: "4"},
	{name: "Susy", img: "https://s-media-cache-ak0.pinimg.com/236x/78/fc/e9/78fce942b36fe0deb62564115aab1170.jpg", id: "2", rating: "3.5"},
	{name: "Lisa", img: "https://s-media-cache-ak0.pinimg.com/736x/54/e8/2a/54e82a4d476d4fba045cbf13fee197ae.jpg", id: "3", rating: "5"}];
*/
});